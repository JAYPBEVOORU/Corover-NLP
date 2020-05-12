package com.corover.nlp.info.extraction.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.corover.nlp.info.extraction.core.NLPCoreEngine;
import com.corover.nlp.info.extraction.dto.NLPParseRequest;
import com.corover.nlp.info.extraction.dto.NLPParseResponse;

@RestController
@RequestMapping("/nlp")
public class NLPExtractController {

	private NLPCoreEngine engine;

	@Autowired
	public NLPExtractController(NLPCoreEngine engine) {
		this.engine = engine;
	}

	@PostMapping("/instant")
	public List<NLPParseResponse> getNLPData(@RequestBody NLPParseRequest request) {
		return engine.analyzeSentence(request.getQuestion());
	}

	@PostMapping("/generatefile")
	public void generateNLPData(@RequestBody NLPParseRequest request) {

	}

}
