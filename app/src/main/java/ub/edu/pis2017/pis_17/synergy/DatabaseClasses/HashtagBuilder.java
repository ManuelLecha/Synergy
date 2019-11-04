package ub.edu.pis2017.pis_17.synergy.DatabaseClasses;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.HashtagDatabase.HashtagModel;
import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;

/**
 * Created by kanales on 07/05/2018.
 */

public class HashtagBuilder implements Builder<HashtagModel,Hashtag> {
    HashtagModel hd;

    public HashtagBuilder setModel(HashtagModel hd) {
        this.hd = hd;
        return this;
    }

    public Hashtag build() {
        return new Hashtag(hd.getHashtagName());
    }
}
