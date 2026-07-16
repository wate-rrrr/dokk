package com.example.demo.service;

import dev.langchain4j.data.document.*;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.internal.chat.AssistantMessage;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.IngestionResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;


public class ChatHelperService{

    Document document = UrlDocumentLoader.load("https://www.alice-in-wonderland.net/wp-content/uploads/alice-in-wonderland.pdf", new TextDocumentParser());

    EmbeddingModel embeddingModel = new OpenAiEmbeddingModel(null);    
    TextSegment textSegment = document.toTextSegment();
    EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

    EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
    .documentSplitter(DocumentSplitters.recursive(300,0))
    .embeddingModel(embeddingModel)
    .embeddingStore(embeddingStore)
    .build();

    ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.from(embeddingStore);

    /**
     * Assistant
     */
    interface Assistant {
        String chat(String userMessage);
        
    }
    
    ChatModel chatModel = OpenAiChatModel.builder()
    .apiKey(System.getenv("HF_API_KEY"))
    .baseUrl("https://router.huggingface.co/v1")
    .modelName("HuggdingFaceTB/SmolLM3-3B:hf-inference")
    .build();
    
    
    


    Assistant assistant = AiServices.builder(Assistant.class)
    .chatModel(chatModel)
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
    .build();

    

    String answer = assistant.chat("who is red queen")



    

    







    
}