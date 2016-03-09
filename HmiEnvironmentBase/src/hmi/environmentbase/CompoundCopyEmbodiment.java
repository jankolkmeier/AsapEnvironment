package hmi.environmentbase;

import java.util.Collection;

import lombok.Getter;

import com.google.common.collect.ImmutableList;

/**
 * CopyEmbodiment consisting of a collection of other CopyEmbodiments, calls copy on them on copy.
 * @author Herwin
 */
public class CompoundCopyEmbodiment implements CopyEmbodiment
{
    private Collection<CopyEmbodiment> embodiments;
    @Getter
    private final String id;
    
    public CompoundCopyEmbodiment(String id, Collection<CopyEmbodiment> embodiments)
    {
        this.embodiments = ImmutableList.copyOf(embodiments);
        this.id = id;
    }
    
    @Override
    public void copy()
    {
        for(CopyEmbodiment e:embodiments)
        {
            e.copy();
        }
    }

}
