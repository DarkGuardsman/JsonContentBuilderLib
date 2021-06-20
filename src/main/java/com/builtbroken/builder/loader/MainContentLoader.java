package com.builtbroken.builder.loader;

import com.builtbroken.builder.ContentBuilderRefs;
import com.builtbroken.builder.pipe.Pipe;
import com.builtbroken.builder.pipe.PipeLine;
import com.builtbroken.builder.pipe.nodes.building.PipeNodeObjectCreator;
import com.builtbroken.builder.pipe.nodes.json.PipeNodeCommentRemover;
import com.builtbroken.builder.pipe.nodes.json.PipeNodeJsonSplitter;
import com.builtbroken.builder.pipe.nodes.mapping.PipeNodeDataMapper;
import com.builtbroken.builder.pipe.nodes.mapping.PipeNodeMappingValidator;
import com.builtbroken.builder.pipe.nodes.mapping.PipeNodeObjectReg;
import com.builtbroken.builder.pipe.nodes.post.PipeNodeAutoWire;
import com.builtbroken.builder.pipe.nodes.post.PipeNodePostValidator;
import com.builtbroken.builder.pipe.nodes.post.PipeNodeWireValidator;

/**
 * Created by Robin Seifert on 6/20/2021.
 */
public class MainContentLoader extends ContentLoader
{
    public MainContentLoader()
    {
        super(ContentBuilderRefs.MAIN_LOADER, new PipeLine());
        logger = (prefix, msg) -> System.out.println("ContentLoader[" + ContentBuilderRefs.MAIN_LOADER + "]:" + prefix + " >> " + msg);
    }

    @Override
    protected void addPipes()
    {
        //Setup cleaner
        Pipe jsonPrepPipe = new Pipe(objectCreationPipeline, ContentBuilderRefs.PIPE_JSON);
        jsonPrepPipe.addNode(new PipeNodeCommentRemover(jsonPrepPipe)); //cleanup
        jsonPrepPipe.addNode(new PipeNodeJsonSplitter()); //breakdown
        objectCreationPipeline.pipes.add(jsonPrepPipe);

        //Setup builder
        Pipe builderPipe = new Pipe(objectCreationPipeline, ContentBuilderRefs.PIPE_BUILDER);
        builderPipe.addNode(new PipeNodeObjectCreator(builderPipe)); //Create json
        objectCreationPipeline.pipes.add(builderPipe);

        //Setup mapper
        Pipe mapperPipe = new Pipe(objectCreationPipeline, ContentBuilderRefs.PIPE_MAPPER);
        mapperPipe.addNode(new PipeNodeDataMapper(mapperPipe)); //map fields
        mapperPipe.addNode(new PipeNodeMappingValidator(mapperPipe)); //validate
        mapperPipe.addNode(new PipeNodeObjectReg(mapperPipe)); //register to handlers
        objectCreationPipeline.pipes.add(mapperPipe);

        //Setup post processing pipeline
        final PipeLine postPipeline = new PipeLine();
        pipelines.add(postPipeline);

        //Add auto wiring to post processing
        final Pipe postPipe = new Pipe(postPipeline, ContentBuilderRefs.PIPE_POST);
        postPipe.addNode(new PipeNodeAutoWire(postPipe)); //wire objects
        postPipe.addNode(new PipeNodeWireValidator(postPipe)); //validate wire
        postPipe.addNode(new PipeNodePostValidator(postPipe)); //validate
        postPipeline.pipes.add(postPipe);
    }
}
