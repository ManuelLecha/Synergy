package ub.edu.pis2017.pis_17.synergy;

import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;

import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;


public class FutureBox<T> {
    /**
     * Created by kanales on 07/05/2018.
     */
    public static class FuturePipeline<T> {
        private final Set<FutureBox<T>> todo;
        private final Queue<Runnable> resolutions;
        private final Queue<Runnable> onerrors;
        boolean done;
        boolean error;

        private FuturePipeline() {
            this.todo = new HashSet<>();
            this.resolutions = new LinkedList<>();
            this.onerrors = new LinkedList<>();
            this.done = true;
            this.error = false;
        }

        public FuturePipeline also(FutureBox f) {
            this.todo.add(f);
            this.done = false;
            f.whenFull(x -> {
                todo.remove(f);
                if (todo.isEmpty()){
                    done = true;
                    while (!resolutions.isEmpty()) {
                        resolutions.remove().run();
                    }
                }
            }).onCancellation((e) -> {
                todo.remove(f);
                error = true;
                //done = true;
                while (!onerrors.isEmpty()) {
                    onerrors.remove().run();
                }
            });
            return this;
        }

        public void resolve(final Runnable cb) {
            if (done) {
                cb.run();
            } else {
                resolutions.add(cb);
            }
        }

        public void ifFails(final Runnable cb) {
            if (error) {
                cb.run();
            } else {
                onerrors.add(cb);
            }
        }
    }

    private Queue<Consumer<T>> todos;
    private Queue<Consumer> todosCancel;

    private T value;
    private boolean cancelled;
    private Object er;

    private FutureBox() {
        value = null;
        cancelled = false;
        todos = new LinkedList<>();
        todosCancel = new LinkedList<>();
    }

    /**
     * Gives a value to the FutureBox
     * @param a
     */
    public boolean give(@NonNull T a) {
        if (isDone() || isCancelled()) return false;
        value = a;
        while (!todos.isEmpty()) {
            todos.remove().accept(value);
        }
        return true;
    }

    public boolean error(Object e) {
        if (isDone()) return false;
        this.cancelled = true;
        this.er = e;
        while (!todosCancel.isEmpty()) {
            todosCancel.remove().accept(e);
        }
        return true;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * @return
     */
    public boolean isDone() {
        return value != null;
    }

    @Deprecated
    public T get() throws InterruptedException, ExecutionException {
        // DO NOT USE
        while(!isDone());
        return value;
    }

    @Deprecated
    public T get(long l, @NonNull TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        long startTime = System.currentTimeMillis(); //fetch starting time
        long endTime = timeUnit.toMillis(l);
        while((System.currentTimeMillis()-startTime) < endTime) {
            if (isCancelled()) throw new CancellationException();
            if (isDone()) return value;
        }
        throw new TimeoutException();
    }

    /**
     * @param cb
     * @return
     */
    public FutureBox<T> whenFull(Consumer<T> cb) {
        if (isDone()) {
            cb.accept(value);
        } else {
            todos.add(cb);
        }
        return this;
    }

    /**
     *
     * @param other
     * @return
     */
    public FuturePipeline<T> also(FutureBox other) {
        FuturePipeline<T> fp = new FuturePipeline();
        fp.also(this).also(other);
        return fp;
    }

    /**
     * Sets the action to do when this is cancelled (due to an er probably)
     * @param r
     * @return
     */
    public FutureBox<T> onCancellation(Consumer r) {
        if (isCancelled()) {
            r.accept(this.er);
        } else {
            todosCancel.add(r);
        }
        return this;
    }

    /**
     * Maps a function f:[A] -> FutureBox[B] to this future box:
     * F[A].flatMap(f) => F[B]
     * @param f
     * @return
     */
    public <Q> FutureBox<Q> flatMap(Function<T,FutureBox<Q>> f) {
        FutureBox<Q> fout = new FutureBox<Q>();
        this.whenFull(x -> f.apply(x).whenFull(a -> {
            fout.give(a);
        }).onCancellation((e) -> fout.error(e)));
        return fout;
    }

    public static <T> FutureBox<T> of(T value) {
        FutureBox<T> futureBox = new FutureBox<T>();
        futureBox.give(value);
        return futureBox;
    }

    public static <T> FutureBox<T> empty() {
        return new FutureBox<>();
    }

    /**
     * flatMap but to map FutureResults
     * @param f
     * @return
     */
    public FutureResult flatMapR(Function<T,FutureResult> f) {
        FutureResult rout = FutureResult.empty();
        this.whenFull(x -> f.apply(x).doOrElse(rout::success,rout::error))
                .onCancellation((e) -> rout.error(e.toString()));
        return rout;
    }

    /**
     * Maps a function to a future box:
     * FutureBox[A](x).map(f); f:A->B => FutureBox[B](f(x))
     * @param f
     * @return FutureBox
     **/
    public <B> FutureBox<B> map(Function<T,B> f) {
        FutureBox<B> fout = FutureBox.empty();
        this.whenFull(x -> {
            if(x==null) fout.error("");
            else fout.give(f.apply(x));
        });
        return fout;
    }

    /**
     * Returns a FuturePipeline from a list of FutureBoxes
     * @param lst
     * @return
     */
    public static FuturePipeline chain(List<FutureBox> lst) {
        FuturePipeline p = new FuturePipeline();
        for (FutureBox b: lst) {
            p.also(b);
        }
        return p;
    }
}
