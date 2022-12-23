package com.github.systeminvecklare.badger.impl.s2dgi.font;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LineSpittingTextLayoutProcedure implements ITextLayoutProcedure {
	private final ILineConstraints lineConstraints;
	private final CharacterEater characterEater = new CharacterEater();
	private final TokenEater tokenEater = new TokenEater();
	private final LineEater lineEater = new LineEater();
	
	public LineSpittingTextLayoutProcedure(ILineConstraints lineConstraints) {
		this.lineConstraints = lineConstraints;
	}

	@Override
	public <C extends Collection<String>> C layoutText(String text, C result) {
		for(int i = 0; i < text.length(); ++i) {
			characterEater.feedCharacter(text.charAt(i));
		}
		characterEater.endOfMeal();
		result.addAll(lineEater.lines);
		return result;
	}
	
	public interface ILineConstraints {
		boolean fits(CharSequence line);
	}
	
	private static class Token {
		public final boolean isWhitespace;
		public final String text;
		
		public Token(boolean isWhitespace, String text) {
			this.isWhitespace = isWhitespace;
			this.text = text;
		}
	}
	
	private class CharacterEater {
		private final StringBuilder stringBuilder = new StringBuilder();
		private boolean buildingWhitespace = true;
		
		public void feedCharacter(char c) {
			if(c == '\n') {
				tokenEater.feedToken(popToken());
				tokenEater.feedToken(new Token(false, "\n"));
				buildingWhitespace = true;
			} else {
				boolean isWhitespace = Character.isWhitespace(c);
				if(isWhitespace == buildingWhitespace) {
					stringBuilder.append(c);
				} else {
					tokenEater.feedToken(popToken());
					stringBuilder.append(c);
					buildingWhitespace = isWhitespace;
				}
			}
		}

		public void endOfMeal() {
			if(stringBuilder.length() > 0) {
				tokenEater.feedToken(popToken());
			}
			tokenEater.endOfMeal();
		}

		private Token popToken() {
			final Token token;
			if(stringBuilder.length() == 0 && buildingWhitespace) {
				token = null;
			} else {
				token = new Token(buildingWhitespace, stringBuilder.toString());
			}
			stringBuilder.setLength(0);
			return token;
		}
	}
	
	private class TokenEater {
		private final List<Token> tokens = new ArrayList<LineSpittingTextLayoutProcedure.Token>();
		private final StringBuilder linePreview = new StringBuilder();
		
		public void feedToken(Token token) {
			if(token != null) { //Just ignore if token is null
				if(token.text.equals("\n")) {
					lineEater.feedLine(popLine());
				} else {
					tokens.add(token);
					setToTokens(linePreview);
					if(!lineConstraints.fits(linePreview) && !token.isWhitespace) {
						tokens.remove(token);
						if(tokens.isEmpty()) {
							if(token.text.length() > 0) {
								int breakPoint = 1;
								while(lineConstraints.fits(token.text.substring(0, breakPoint))) {
									breakPoint++;
								}
								if(breakPoint > 1) {
									breakPoint--;
								}
								lineEater.feedLine(token.text.substring(0, breakPoint));
								feedToken(new Token(token.isWhitespace, token.text.substring(breakPoint)));
							}
						} else {
							lineEater.feedLine(popLine());
							feedToken(token);
						}
					}
				}
			}
		}

		private void setToTokens(StringBuilder lineBuilder) {
			lineBuilder.setLength(0);
			for(Token token : tokens) {
				lineBuilder.append(token.text);
			}
		}

		private String popLine() {
			trimTrailingWhitespace();
			StringBuilder builder = new StringBuilder();
			for(Token token : tokens) {
				builder.append(token.text);
			}
			tokens.clear();
			return builder.toString();
		}

		private void trimTrailingWhitespace() {
			Collections.reverse(tokens);
			Iterator<Token> iterator = tokens.iterator();
			while(iterator.hasNext()) {
				if(!iterator.next().isWhitespace) {
					break;
				} else {
					iterator.remove();
				}
			}
			Collections.reverse(tokens);
		}

		public void endOfMeal() {
			if(!tokens.isEmpty()) {
				lineEater.feedLine(popLine());
			}
		}
	}
	
	private class LineEater {
		private final List<String> lines = new ArrayList<>();
		public void feedLine(String line) {
			lines.add(line);
		}
	}
}
