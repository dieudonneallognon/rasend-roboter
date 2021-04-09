
public class FaceRotator {


	public static Face rotate(Face face, RotationDegree degree) {

		switch (degree) {
		case ZERO: {
			return face;
		}

		case CIRCLE_QUATER: {
			return rotate90(face);
		}

		case CIRCLE_HALF: {
			return rotate180(face);
		}

		case THREE_CIRCLE_QUATER: {
			return rotate270(face);
		}
		}
		return null;
	}


	private static Face rotate270(Face face) {

		Case[][] cases = face.getCases();

		Case[][] rotated = new Case[FaceGenerator.CASE_NUMBER][FaceGenerator.CASE_NUMBER];

		for(int i = 0;i<FaceGenerator.CASE_NUMBER;++i) {
			for(int j = 0;j<FaceGenerator.CASE_NUMBER;++j) {

				cases[i][j].setCoord(new Board.Coord(
						FaceGenerator.CASE_NUMBER - 1 - cases[i][j].getCoord().getColumn(),
						cases[i][j].getCoord().getRow()));
				
				rotated[i][j] = rotateBorder(rotateBorder(rotateBorder(cases[i][j])));
			}
		}

		return new Face(rotated);
	}


	private static Face rotate180(Face face) {

		Case[][] cases = face.getCases();

		Case[][] rotated = new Case[FaceGenerator.CASE_NUMBER][FaceGenerator.CASE_NUMBER];

		for(int i = 0;i<FaceGenerator.CASE_NUMBER;++i) {
			for(int j = 0;j<FaceGenerator.CASE_NUMBER;++j) {

				cases[i][j].setCoord(new Board.Coord(
						FaceGenerator.CASE_NUMBER - 1 - cases[i][j].getCoord().getRow(),
						FaceGenerator.CASE_NUMBER - 1 -cases[i][j].getCoord().getColumn()));
				
				rotated[i][j] = rotateBorder(rotateBorder(cases[i][j]));
				
			}
		}

		return new Face(rotated);
	}

	private static Face rotate90(Face face) {

		Case[][] cases = face.getCases();

		Case[][] rotated = new Case[FaceGenerator.CASE_NUMBER][FaceGenerator.CASE_NUMBER];


		for(int i = 0; i < FaceGenerator.CASE_NUMBER; ++i) {
			for(int j = 0; j < FaceGenerator.CASE_NUMBER; ++j) {

				cases[i][j].setCoord(new Board.Coord(
						cases[i][j].getCoord().getColumn(),
						FaceGenerator.CASE_NUMBER - 1 -cases[i][j].getCoord().getRow()));
				
				rotated[i][j] = rotateBorder(cases[i][j]);
				
				//
//				if (!cases[i][j].canCrossTop()) {
//					rotated, i, j);
//				}
//
//				if (!cases[i][j].canCrossLeft()) {
//					BorderGenerator.placeTopBorderOn(rotated, i, j);
//				}
//
//				if (!cases[i][j].canCrossDown()) {
//					BorderGenerator.placeLeftBorderOn(rotated, i, j);
//				}
//
//				if (!cases[i][j].canCrossRight()) {
//					BorderGenerator.placeBottomBorderOn(rotated, i, j);
//				}

			}
		}

		return new Face(rotated);
	}
	
	private static Case rotateBorder(Case in) {
		
		return new Case(in.getCoord(), in.getColor(), in.getType(), !in.canCrossLeft(), !in.canCrossRight(), !in.canCrossDown(), !in.canCrossTop()); 
	}
	

}