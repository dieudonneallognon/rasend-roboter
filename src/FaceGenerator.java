public class FaceGenerator {
	
	public final static int CASE_NUMBER = 8;
	
	public static Face generateFace(FaceID faceId, FacePosition position) {
		
		Face face = null;
		
		switch (faceId) {
		case ONE: {
			face = generateFace1(); 
			break;
		}
		case TWO: {
			face = generateFace2();
			break;
		}
		case TRHEE: {
			face = generateFace3();
			break;
		}
		case FOUR: {
			face = generateFace4();
			break;
		}
		case FIVE: {
			face = generateFace5();
			break;
		}
		case SIX: {
			face = generateFace6();
			break;
		}
		case SEVEN: {
			face = generateFace7();
			break;
		}
		case EIGHT: {
			face = generateFace8();
			break;
		}
		}
				
		switch (position) {
		case TOP_RIGHT:
			return face;
		case BOTTOM_RIGHT:
			return FaceRotator.rotate(face, RotationDegree.CIRCLE_QUATER);
		case BOTTOM_LEFT:
			return FaceRotator.rotate(face, RotationDegree.CIRCLE_HALF);
		case TOP_LEFT:
			return FaceRotator.rotate(face, RotationDegree.THREE_CIRCLE_QUATER);
		}
		return face;
	}
	
	
	private static Face generateFace8() {
		Case[][] tmp = initFaceCoord();
		 
		
		for(int i = 0;i< CASE_NUMBER;++i) {
			for (int j = 0;j< CASE_NUMBER;++j) {
				
				initFaceCenterBorders(tmp, i, j);
				initFaceBorders(tmp, i, j);
				
				if (i == 0 && j ==2) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
				}
				
				if (i == 1 && j == 4) {
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.VERT);
					tmp[i][j].setType(CaseType.SATURNE);
				}
				
				if (i == 2 && j == 6) {
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.ROUGE);
					tmp[i][j].setType(CaseType.CROIX);
				}
				
				if (i == 3 && j == 0) {
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					tmp[i][j].setType(CaseType.TROU_NOIR);
				}
				
				if (i == 4 && j == 7) {
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
				}
				
				if(i == 5 && j == 1) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.BLEU);
					tmp[i][j].setType(CaseType.CROISSANT);
				}
				
				if (i == 6 && j == 3) {
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.JAUNE);
					tmp[i][j].setType(CaseType.ENGRENAGE);
				}				
				///System.out.println(tmp[i][j]);
			}
		}
		return new Face(tmp);
	}


	private static Face generateFace7() {
		Case[][] tmp = initFaceCoord();
		
		for(int i = 0;i< CASE_NUMBER;++i) {
			for (int j = 0;j< CASE_NUMBER;++j) {
				
				initFaceCenterBorders(tmp, i, j);
				initFaceBorders(tmp, i, j);
				
				if (i == 0 && j == 0) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
				}	
				
				if (i == 1 && j == 4) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.JAUNE);
					tmp[i][j].setType(CaseType.ENGRENAGE);
				}				
				
				if (i == 2 && j == 2) {
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.ROUGE);
					tmp[i][j].setType(CaseType.CROIX);
				}
				
				if (i == 3 && j == 7) {
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
				}
				
				if (i == 5 && j == 3) {
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.VERT);
					tmp[i][j].setType(CaseType.SATURNE);
				}
				
				if(i == 6 && j == 6) {
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.BLEU);
					tmp[i][j].setType(CaseType.CROISSANT);
				}
				
				if (i == 7 && j == 2) {
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					tmp[i][j].setType(CaseType.TROU_NOIR);
				}				
			}
		}
		return new Face(tmp);
	}


	private static Face generateFace6() {
		Case[][] tmp = initFaceCoord();
		
		for(int i = 0;i< CASE_NUMBER;++i) {
			for (int j = 0;j< CASE_NUMBER;++j) {
				initFaceCenterBorders(tmp, i, j);
				initFaceBorders(tmp, i, j);
				
				if (i == 0 && j == 1) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
				}
				
				if (i == 1 && j == 5) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.VERT);
					tmp[i][j].setType(CaseType.ENGRENAGE);
				}
				
				if (i == 1 && j == 7) {
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
				}
				
				if (i == 3 && j == 1) {
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.BLEU);
					tmp[i][j].setType(CaseType.SATURNE);
				}
				
				if (i == 4 && j == 6) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.ROUGE);
					tmp[i][j].setType(CaseType.CROISSANT);
				}
				
				
				if(i == 6 && j == 4) {
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.JAUNE);
					tmp[i][j].setType(CaseType.CROIX);
				}
			}
		}
		return new Face(tmp);
	}


	private static Face generateFace5() {
		Case[][] tmp = initFaceCoord();
		
		for(int i = 0;i< CASE_NUMBER;++i) {
			for (int j = 0;j< CASE_NUMBER;++j) {
				initFaceCenterBorders(tmp, i, j);
				initFaceBorders(tmp, i, j);
				
				if (i == 0 && j == 1) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
				}
				
				if (i == 1 && j == 5) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.VERT);
					tmp[i][j].setType(CaseType.ENGRENAGE);
				}
				
				if (i == 3 && j == 1) {
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.ROUGE);
					tmp[i][j].setType(CaseType.CROISSANT);
				}
				
				if (i == 4 && j == 7) {
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
				}
				
				if (i == 6 && j == 2) {
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.BLEU);
					tmp[i][j].setType(CaseType.SATURNE);
				}
				
				
				if(i == 6 && j == 6) {
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.JAUNE);
					tmp[i][j].setType(CaseType.CROIX);
				}
			}
		}
		return new Face(tmp);
	}


	private static Face generateFace4() {
		Case[][] tmp = initFaceCoord();
		
		for(int i = 0;i< CASE_NUMBER;++i) {
			for (int j = 0;j< CASE_NUMBER;++j) {
				
				initFaceCenterBorders(tmp, i, j);
				initFaceBorders(tmp, i, j);
				
				if (i == 0 && j == 3) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
				}
				
				if (i == 1 && j == 5) {
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.ROUGE);
					tmp[i][j].setType(CaseType.SATURNE);
				}
				
				if (i == 2 && j == 1) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.BLEU);
					tmp[i][j].setType(CaseType.ENGRENAGE);
				}
				
				if (i == 3 && j == 7) {
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
				}
				
				if (i == 5 && j == 6) {
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.VERT);
					tmp[i][j].setType(CaseType.CROIX);
				}
				
				
				if(i == 6 && j == 3) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.JAUNE);
					tmp[i][j].setType(CaseType.CROISSANT);
				}
			}
		}
		return new Face(tmp);
	}


	private static Face generateFace3() {
		Case[][] tmp = initFaceCoord();
		
		for(int i = 0;i< CASE_NUMBER;++i) {
			for (int j = 0;j< CASE_NUMBER;++j) {
				
				initFaceCenterBorders(tmp, i, j);
				initFaceBorders(tmp, i, j);
				
				if (i == 0 && j == 2) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
				}
				
				if (i == 1 && j == 1) {
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.VERT);
					tmp[i][j].setType(CaseType.CROIX);
				}
				
				if(i == 2 && j == 6) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.JAUNE);
					tmp[i][j].setType(CaseType.CROISSANT);
				}
				
				if (i == 4 && j == 2) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.ROUGE);
					tmp[i][j].setType(CaseType.SATURNE);
				}
				
				
				if (i == 4 && j == 7) {
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
				}
				
				if (i == 6 && j == 4) {
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.BLEU);
					tmp[i][j].setType(CaseType.ENGRENAGE);
				}
			}
		}
		return new Face(tmp);
	}


	private static Face generateFace2() {
		Case[][] tmp = initFaceCoord();
		
		for(int i = 0;i< CASE_NUMBER;++i) {
			for (int j = 0;j< CASE_NUMBER;++j) {
				
				initFaceCenterBorders(tmp, i, j);
				initFaceBorders(tmp, i, j);
				
				if (i == 0 && j == 1) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
				}
				
				if (i == 1 && j == 6) {
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.ROUGE);
					tmp[i][j].setType(CaseType.ENGRENAGE);
				}
				
				if(i == 2 && j == 3) {
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.BLEU);
					tmp[i][j].setType(CaseType.CROIX);
				}
				
				if (i == 3 && j == 7) {
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
				}
				
				if (i == 6 && j == 5) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.VERT);
					tmp[i][j].setType(CaseType.CROISSANT);
				}
				
				if (i == 7 && j == 2) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.JAUNE);
					tmp[i][j].setType(CaseType.SATURNE);
				}
				
			}
				
		}
		return new Face(tmp);
	}


	private static Face generateFace1() {
		
		Case[][] tmp = initFaceCoord();
		
		for(int i = 0;i< CASE_NUMBER;++i) {
			for (int j = 0;j< CASE_NUMBER;++j) {
				
				initFaceCenterBorders(tmp, i, j);
				initFaceBorders(tmp, i, j);
				
				if (i == 0 && j == 2) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
				}
				
				if (i == 1 && j == 1) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.JAUNE);
					tmp[i][j].setType(CaseType.SATURNE);
				}
				
				
				if (i == 2 && j == 3) {
					BorderGenerator.placeRightBorderOn(tmp, i, j);
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.VERT);
					tmp[i][j].setType(CaseType.CROISSANT);
				}
				
				if (i == 3 && j == 7) {
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
				}
				
				if(i == 5 && j == 5) {
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					BorderGenerator.placeBottomBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.BLEU);
					tmp[i][j].setType(CaseType.CROIX);
				}
				
				if (i == 7 && j == 2) {
					BorderGenerator.placeTopBorderOn(tmp, i, j);
					BorderGenerator.placeLeftBorderOn(tmp, i, j);
					tmp[i][j].setColor(GameColor.ROUGE);
					tmp[i][j].setType(CaseType.ENGRENAGE);
				}
			}
		}
		return new Face(tmp);
	}
	
	private static Case[][] initFaceCoord() {
		
		Case[][] tmp = new Case[CASE_NUMBER][CASE_NUMBER];
		
		for(int i = 0; i< CASE_NUMBER;++i) {
			for (int j = 0; j< CASE_NUMBER;++j) {
				tmp[i][j] = new Case();
				tmp[i][j].setCoord(new Board.Coord(i, j));
			}
		}
		
		return tmp;
	}
	
	private static void initFaceBorders(Case[][] tmp, int i, int j) {
		
		if(i == 0) {
			tmp[i][j].setTopBorder(true);
		}
		
		if(j == 7) {
			tmp[i][j].setRightBorder(true);
		}
		
	}
	
	private static void initFaceCenterBorders(Case[][] tmp, int i, int j) {
		if (i == 7 && j == 0) {
			tmp[i][j].setTopBorder(true);
			tmp[i][j].setRightBorder(true);

			tmp[i-1][j].setBottomBorder(true);
			tmp[i][j+1].setLeftBorder(true);
		}
	}
	
	
	
	
	
	
}
