package io.languagetoys.errant4j.lang.en;

import io.languagetoys.errant4j.core.Pipeline;
import io.languagetoys.errant4j.core.classify.Classifier;
import io.languagetoys.errant4j.core.merge.Merger;
import io.languagetoys.errant4j.lang.en.classify.EnClassifier;
import io.languagetoys.errant4j.lang.en.merge.EnMerger;
import io.languagetoys.errant4j.lang.en.utils.lemmatize.WordNetLemmatizer;
import io.languagetoys.errant4j.lang.en.utils.wordlist.HunspellWordList;

public class EnPipeline extends Pipeline.Base {
    
    @Override
    public Merger getMerger() {
        return new EnMerger();
    }

    @Override
    public Classifier getClassifier() {
        return new EnClassifier(new WordNetLemmatizer(), new HunspellWordList());
    }
}
