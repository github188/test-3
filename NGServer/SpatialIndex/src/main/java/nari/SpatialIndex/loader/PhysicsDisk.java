package nari.SpatialIndex.loader;

import java.io.File;

public class PhysicsDisk extends AbstractDisk {
	
//	private String path;
//	
//	private String root;
	
	private File file;
	
//	private Disk parent;
	
//	public PhysicsDisk(File file){
//		this.file = file;
//	}
	
	public PhysicsDisk(Disk disk,String root){
		if(disk==null){
			this.file = new File(root);
		}else{
			try {
				this.file = new File(disk.getFile(),root);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
//	public PhysicsDisk(Disk parent){
//		super(parent);
//		this.parent = parent;
//	}

	protected void makeDir() throws Exception{
		file.mkdir();
	}
	
	protected void doMakeFile() throws Exception{
		file.createNewFile();
	}

	@Override
	protected void doGetDisk(String name) throws Exception {
//		String p = "";
//		if(getPath().endsWith(File.separator)){
//			p = getPath() + name + File.separator;
//		}else{
//			p = getPath() + File.separator + name + File.separator;
//		}
//		file = new File(p);
//		file = new File(file,name);
	}

	@Override
	protected boolean exists() throws Exception {
		return file.exists();
	}

	@Override
	public String getPath() {
		return "";
	}

	@Override
	public String getRoot() {
		return "";
	}

	@Override
	public File getFile() throws Exception {
		return file;
	}
}
