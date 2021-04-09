import java.util.ArrayList;

public class Face {
	
	private Case[][] caseList;
	
	public Face(Case[][] cases) {
		this.caseList = cases;
	}
	
	public Case[][]getCases() {
		return this.caseList;
	}
	
	@Override
	public String toString() {
		
		String str = "";
		
		
		for(int i = 0;i< caseList.length;++i) {
			for (int j = 0;j< caseList.length;++j) {
				
				str += caseList[i][j].getCoord().toString() + "\t";
			}
			str += '\n';
		}
		return str;
	}
	
}
