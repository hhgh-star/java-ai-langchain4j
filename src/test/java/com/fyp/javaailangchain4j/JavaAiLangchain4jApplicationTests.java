package com.fyp.javaailangchain4j;

import com.fyp.javaailangchain4j.assistant.Assistant;
import com.fyp.javaailangchain4j.domain.Appointment;
import com.fyp.javaailangchain4j.service.AppointmentService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
class JavaAiLangchain4jApplicationTests
{
	
	@Resource
	private EmbeddingModel embeddingModel;
	
	@Resource
	private EmbeddingStore embeddingStore;
	@Test
	void testUploadDocumentsLibrary(){
		List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/documents");
		EmbeddingStoreIngestor.builder()
			.embeddingModel(embeddingModel)
			.embeddingStore(embeddingStore)
			.build()
			.ingest(documents);
	}
}
