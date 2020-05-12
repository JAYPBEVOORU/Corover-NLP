package com.corover.nlp.info.extraction.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class NLPParseRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7327312890919950297L;
	private String question;
	private String inputPath;
	private String outputPath;

}
