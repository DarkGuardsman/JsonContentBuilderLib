package com.builtbroken.builder.pipe.nodes;

import com.builtbroken.builder.converter.ConversionHandler;
import com.builtbroken.builder.loader.ContentLoader;
import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.PipeLine;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-05.
 */
public abstract class PipeNode implements IPipeNode
{

    protected final Pipe pipe;
    private final NodeType type;
    private final String id;

    protected PipeNode(Pipe pipe, NodeType type, String id)
    {
        this.pipe = pipe;
        this.type = type;
        this.id = id;
    }

    @Override
    public NodeType getNodeType()
    {
        return type;
    }

    @Override
    public String getUniqueID()
    {
        return id;
    }

    protected ContentLoader getContentLoader()
    {
        return pipe != null ? pipe.getLoader() : null;
    }

    protected PipeLine getPipeLine()
    {
        return pipe != null ? pipe.getPipeLine() : null;
    }

    protected ConversionHandler getConverter()
    {
        return pipe != null ? pipe.getConverter() : null;
    }
}
