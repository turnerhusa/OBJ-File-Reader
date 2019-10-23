import java.util.ArrayList;

public class TriObject {
	private ArrayList<Triangle> tris;
	
	public TriObject(){
		tris = new ArrayList<Triangle>();
	}
	
	public TriObject(ArrayList<Triangle> tris){
		this.tris = tris;
	}
	
	public void addTriangle(Triangle t){
		tris.add(t);
	}
	
	public ArrayList<Triangle> getTriangles(){
		return tris;
	}
	
	/*private void inflate() {
	    ArrayList<Triangle> result = new ArrayList<Triangle>();
	    for (Triangle t : this.tris) {
	        Vertex m1 =
	            new Vertex((t.v1.x + t.v2.x)/2, (t.v1.y + t.v2.y)/2, (t.v1.z + t.v2.z)/2);
	        Vertex m2 =
	            new Vertex((t.v2.x + t.v3.x)/2, (t.v2.y + t.v3.y)/2, (t.v2.z + t.v3.z)/2);
	        Vertex m3 =
	            new Vertex((t.v1.x + t.v3.x)/2, (t.v1.y + t.v3.y)/2, (t.v1.z + t.v3.z)/2);
	        result.add(new Triangle(t.v1, m1, m3, t.color));
	        result.add(new Triangle(t.v2, m1, m2, t.color));
	        result.add(new Triangle(t.v3, m2, m3, t.color));
	        result.add(new Triangle(m1, m2, m3, t.color));
	    }
	    for (Triangle t : result) {
	        for (Vertex v : new Vertex[] { t.v1, t.v2, t.v3 }) {
	            double l = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z) / Math.sqrt(30000);
	            v.x /= l;
	            v.y /= l;
	            v.z /= l;
	        }
	    }
	    tris = result;
	}
	
	public TriObject inflated_x(int times){
		if (times >= 1){
			TriObject result = new TriObject(this.tris);
			for (int i = 0; i < times; i++){
				result.inflate();
			}
			return result;
		}else{
			return this;
		}
	}*/
}
