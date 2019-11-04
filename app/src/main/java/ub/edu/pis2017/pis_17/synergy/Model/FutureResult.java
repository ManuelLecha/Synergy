package ub.edu.pis2017.pis_17.synergy.Model;

import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import ub.edu.pis2017.pis_17.synergy.FutureBox;

/**
 * Created by kanales on 18/04/2018.
 */

public class FutureResult {
    /**
     * Created by kanales on 07/05/2018.
     */
    private enum Value {
        OK,
        ERR,
        WAITING
    }
    private String defaultError;

    private Queue<Runnable> todos;
    private Queue<Consumer<String>> todosCancel;

    private String error;
    private Value value;

    private FutureResult() {
        value = Value.WAITING;
        todos = new LinkedList<>();
        todosCancel = new LinkedList<>();
    }

    public static FutureResult empty() {
        return new FutureResult();
    }

    public FutureResult setDefaultError(String error) {
        this.defaultError = error;
        return this;
    }

    /**
     * Gives a value to the FutureBox
     */
    public boolean success() {
        if (isError()) return false;
        this.value = Value.OK;
        while (!todos.isEmpty()) todos.remove().run();
        return true;
    }

    public boolean error(String err) {
        if (isOk()) return false;
        this.error = err;
        this.value = Value.ERR;
        while (!todosCancel.isEmpty()) {
            todosCancel.remove().accept(err);
        }
        return true;
    }

    public boolean error() {
        error((defaultError == null)?"":defaultError);
        return (defaultError == null);
    }

    /**
     * @return
     */
    public boolean isOk() {
        return value == Value.OK;
    }

    /**
     * @param cb
     * @return
     */
    public FutureResult whenDone(Runnable cb) {
        if (isOk()) {
            cb.run();
        } else {
            todos.add(cb);
        }
        return this;
    }

    public FutureResult ifFails(Consumer<String> c) {
        if (isOk()) return this;
        if (isError()) {
            c.accept(this.error);
            return this;
        }
        todosCancel.add(c);
        return this;
    }

    public boolean isError() {
        return this.value == Value.ERR;
    }

    public FutureResult then(Supplier<FutureResult>  p) {
        switch (this.value) {
            case OK:
                return p.get();
            case ERR:
                return this;
            default:
            case WAITING:
                this.whenDone(() -> p.get());
                return this;
        }
    }

    public FutureResult doOrElse(Runnable x, Consumer<String> y) {
        switch (this.value) {
            case OK:
                x.run();
                return this;
            case ERR:
                y.accept(error);
                return this;
            default:
            case WAITING:
                this.whenDone(x);
                this.ifFails(y);
                return this;
        }
    }

    public static FutureResult ofSuccess() {
        FutureResult res = FutureResult.empty();
        res.success();
        return res;
    }
}