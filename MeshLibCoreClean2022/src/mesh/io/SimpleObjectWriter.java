package mesh.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class SimpleObjectWriter {

    private int vertexOffset;
    private StringBuffer buffer;
    private String objectName;
    private File file;
    private Mesh3D mesh;

    public SimpleObjectWriter(File file) {
	this.file = file;
	initializeBuffer();
    }

    private void writeVerticesToBuffer() {
	for (Vector3f vertex : mesh.vertices)
	    writeVertex(vertex);
    }

    private void writeVertex(Vector3f vertex) {
	append("v ");
	append(vertex.x);
	append(" ");
	append(vertex.y);
	append(" ");
	append(vertex.z);
	append("\n");
    }

    private void writeObjectNameToBuffer() {
	append("o ");
	append(objectName);
	append("\n");
    }

    private void writeFacesToBuffer() {
	for (Face3D f : mesh.faces) {
	    buffer.append("f ");
	    for (int i = 0; i < f.indices.length; i++) {
		append(f.indices[i] + 1 + vertexOffset);
		append(" ");
	    }
	    append("\n");
	}
    }

    private void writeBufferToFile() throws IOException {
	BufferedWriter out = new BufferedWriter(new FileWriter(file));
	out.write(buffer.toString());
	out.flush();
	out.close();
    }

    public void write(Mesh3D mesh, String objectName) throws IOException {
	setObjectName(objectName);
	setMesh(mesh);
	writeObjectNameToBuffer();
	writeVerticesToBuffer();
	writeFacesToBuffer();
	vertexOffset += mesh.getVertexCount();
    }

    public void close() throws IOException {
	writeBufferToFile();
    }

    private void append(String str) {
	buffer.append(str);
    }

    private void append(float value) {
	buffer.append(value);
    }

    private void append(int value) {
	buffer.append(value);
    }

    private void initializeBuffer() {
	buffer = new StringBuffer();
    }

    private void setMesh(Mesh3D mesh) {
	this.mesh = mesh;
    }

    private void setObjectName(String objectName) {
	this.objectName = objectName;
    }

}
