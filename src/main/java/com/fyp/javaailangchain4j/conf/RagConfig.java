package com.fyp.javaailangchain4j.conf;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.HuggingFaceTokenCountEstimator;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @Project java-ai-langchain4j
 * @Author 房毅鹏
 * @Create 2026-05-01 15:15:13
 * @Description
 */
@Configuration
public class RagConfig
{
	@Resource
	private EmbeddingStore embeddingStore;
	
	@Resource
	private EmbeddingModel embeddingModel;
	@Bean
	public ContentRetriever contentRetriever()
	{
		
		//1.加载文档
		List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/documents");
		//2.文档切割，将文档按照段落进行分割，每个片段不超过300个token，并且有30个token的重叠部分保证连贯性
		DocumentByParagraphSplitter documentByParagraphSplitter = new DocumentByParagraphSplitter(
			300,
			30,
			//token分词计算器,按token计算
			new HuggingFaceTokenCountEstimator());
		EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
			//使用的向量模型
			.embeddingModel(embeddingModel)
			.embeddingStore(embeddingStore)
			.documentSplitter(documentByParagraphSplitter)
			//为了提高文档的质量，为每个切割后的文档碎片 TextSegment 添加文档名称做为元信息
			.textSegmentTransformer(textSegment -> TextSegment.from(textSegment.metadata().getString("file_name")
				+ "\n" + textSegment.text(),textSegment.metadata()))
			.build();
		ingestor.ingest(documents);
		return EmbeddingStoreContentRetriever.builder()
			// 设置用于生成嵌入向量的嵌入模型
			.embeddingModel(embeddingModel)
			// 指定要使用的嵌入存储
			.embeddingStore(embeddingStore)
			// 设置最大检索结果数量，这里表示最多返回 1 条匹配结果
			.maxResults(1)
			// 设置最小得分阈值，只有得分大于等于 0.8 的结果才会被返回
			.minScore(0.8)
			// 构建最终的 EmbeddingStoreContentRetriever 实例
			.build();
	}
}
