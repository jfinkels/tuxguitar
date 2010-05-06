package org.herac.tuxguitar.player.impl.midiport.coreaudio;

import java.io.File;

public class JNILibraryLoader {
	
	private static final String JNI_EXTENSION = ".jnilib";	
	
	//private static final String JNI_TMP_PATH = (System.getProperty( "java.io.tmpdir" ) + File.separator);
	 /** The Logger for this class. */
  public static final transient Logger LOG = Logger.getLogger(JNILibraryLoader.class);

	public static void loadLibrary(String libname){
		LOG.debug"trying to load" + libname + " (void loadLibrary)");
		JNILibraryLoader.loadFromClassPath(libname + JNI_EXTENSION);
		/*
		if(!JNILibraryLoader.loadFromClassPath(libname + JNI_EXTENSION)){
			//System.loadLibrary(libname);
		}
		 */
	}
	
	private static boolean loadFromClassPath(String filename){
		LOG.debug("trying to load" + filename + " (bool loadFromClassPath)");
		
		File file = new File(/*JNI_TMP_PATH +*/ filename);
		/*
		try{
			if(!file.exists()){
				OutputStream outputStream = new FileOutputStream(file);
				InputStream inputStream = JNILibraryLoader.class.getClassLoader().getResourceAsStream(filename);
				if (inputStream != null) {
					int read;
					byte [] buffer = new byte [4096];
					while ((read = inputStream.read (buffer)) != -1) {
						outputStream.write(buffer, 0, read);
					}
					outputStream.close();
					inputStream.close();
				}
			}
			*/
			if(file.exists()){
				LOG.debug("calling file.getAbsolutePath() : "+ file.getAbsolutePath());
				System.load(file.getAbsolutePath());
				//System.load(file.getAbsolutePath());
				return true;
			}
			//else
			//{
				LOG.debug("Can't find file " + file.getAbsolutePath());
				return false;
			//}
			/*
		}catch(Throwable throwable){
			return false;
		}finally{
			if(file.exists()){
				file.delete();
			}
		}
		return false;
			 */
	}
}	
	

