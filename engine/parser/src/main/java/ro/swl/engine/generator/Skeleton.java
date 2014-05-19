package ro.swl.engine.generator;

import java.io.File;


public class Skeleton {

	public static final String SKELETON_ROOT = "skeletons";

	private File skeletonRootDir = new File(SKELETON_ROOT);

	private File skeletonInstanceDir;


	public Skeleton() {
	}


	public Skeleton(String skeletonName) {
		this.skeletonInstanceDir = new File(skeletonRootDir, skeletonName);
	}


	public File getSkeletonInstanceDir() {
		return skeletonInstanceDir;
	}



	public File getSkeletonRootDir() {
		return skeletonRootDir;
	}


	public void setSkeletonRootDir(String skeletonRootDir) {
		this.skeletonRootDir = new File(skeletonRootDir);
	}


	public void setSkeletonName(String skeletonName) {
		this.skeletonInstanceDir = new File(skeletonRootDir, skeletonName);
	}


	public void setSkeletonRootDir(File skeletonRootDir) {
		this.skeletonRootDir = skeletonRootDir;
	}



}
