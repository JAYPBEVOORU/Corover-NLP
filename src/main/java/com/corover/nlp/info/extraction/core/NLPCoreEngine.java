package com.corover.nlp.info.extraction.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.corover.nlp.info.extraction.dto.NLPParseResponse;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.util.Pair;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NLPCoreEngine {

	StanfordCoreNLP pipeline;

	@PostConstruct
	public void init() {
		Properties props = new Properties();
		/** set these annotators as per the requirement */
		/** "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote" */
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp");
		props.setProperty("coref.algorithm", "neural");
		this.pipeline = new StanfordCoreNLP(props);
	}

	public List<NLPParseResponse> analyzeSentence(String sentenceStr) {
		CoreDocument document = new CoreDocument(sentenceStr);
		pipeline.annotate(document);
		List<NLPParseResponse> responses = new ArrayList<>();
		List<CoreSentence> sentences = document.sentences();
		for (CoreSentence sentence : sentences) {
			log.info("------------------------------------");
			log.info(sentence.text());
			log.info("------------------------------------");

			NLPParseResponse response = new NLPParseResponse();

			response = retrieveObjective(sentence, response);

			/** objective is empty fetch random compound */
			if (null == response.getIntent() || null == response.getEntity()) {
				response = retrieveCompoundIfNotObj(sentence, response);
			}
			log.info("response: {}", response);
			responses.add(response);
		}
		return responses;
	}

	private NLPParseResponse retrieveObjective(CoreSentence sentence, NLPParseResponse response) {
		for (SemanticGraphEdge edge : sentence.dependencyParse().edgeListSorted()) {
			if (edge.getRelation().toString().equalsIgnoreCase("obj")
					|| edge.getRelation().toString().equalsIgnoreCase("dep")
					|| edge.getRelation().toString().equalsIgnoreCase("acl")) {

				response.setIntent(edge.getSource().toString());
				response.setEntity(edge.getTarget().toString());

				List<Pair<GrammaticalRelation, IndexedWord>> childPairs = sentence.dependencyParse()
						.childPairs(edge.getTarget());
				if (!CollectionUtils.isEmpty(childPairs)) {
					for (Pair<GrammaticalRelation, IndexedWord> pair : childPairs) {
						if (pair.first().toString().equalsIgnoreCase("compound")) {
							response.setEntityChild(pair.second().toString());
						}
					}
				}
			}

			Set<IndexedWord> parentsWithReln = sentence.dependencyParse().getParentsWithReln(edge.getSource(),
					"compound");
			parentsWithReln.forEach(parent -> {
				/** check this to retrieve only one compound relation */
				response.setIntentParent(parent.toString());
			});
		}
		return response;
	}

	private NLPParseResponse retrieveCompoundIfNotObj(CoreSentence sentence, NLPParseResponse response) {
		for (SemanticGraphEdge edge : sentence.dependencyParse().edgeListSorted()) {
			if (edge.getRelation().toString().equalsIgnoreCase("compound")) {
				response.addCompounds(edge.getSource().toString(), edge.getTarget().toString());
			}
		}
		return response;
	}
}
