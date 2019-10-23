
public class Matrix4 {
	double [] values;
	public Matrix4 (double[] values){
		this.values = values;
	}
	
	public Matrix4 multiply(Matrix4 other){
		double[] result = new double[16];
		for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                for (int i = 0; i < 4; i++) {
                    result[row * 4 + col] += this.values[row * 4 + i] * other.values[i * 4 + col];
                }
            }
        }
		return new Matrix4(result);
	}
	
	public Vertex translate(Vertex in , double factor){
		return new Vertex(
				in.x + values[3]*-10,
                in.y + values[7]*-10,
                in.z + values[11]*-10,
                in.w
		);
	}
	
	public Vertex transform(Vertex in){
		return new Vertex(
				 in.x * values[0] + in.y * values[4] + in.z * values[8] + in.w * values[12],
                 in.x * values[1] + in.y * values[5] + in.z * values[9] + in.w * values[13],
                 in.x * values[2] + in.y * values[6] + in.z * values[10] + in.w * values[14],
                 in.x * values[3] + in.y * values[7] + in.z * values[11] + in.w * values[15]
	        );
	}
}
