package resources;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import util.IO;
import util.Log;

public class RBuilder {
	
	private static final char SEPERATOR = java.io.File.separatorChar;
	private static final String PACKAGE = "resources";
	private static final String[] IMPORTS = {
		"import java.awt.Font;\n",
		"import graphics.SpriteSheet;\n",
		"import graphics.PixelImage;\n",
		"import util.IO;\n",
//		"import javax.sound.sampled.Clip;\n"
	};
	
	private static final List<FileHandler> fileHandlers = new ArrayList<FileHandler>();
	static {
		fileHandlers.add(new TTFHandler());
		fileHandlers.add(new PNGHandler());
		fileHandlers.add(new SpriteHandler());
		fileHandlers.add(new WAVHandler());
		//fileHandlers.add(new WAVClipHandler());
	}
	
	private static String getIndent(int depth) {
		return new String(new char[depth]).replace("\0", "\t");
	}
	
	public static String getExtension(String s) {
		return s.substring(s.lastIndexOf(".") + 1);
	}
	
	private static void parse(StringBuilder s, String root) {
		//package
		s.append("package ").append(PACKAGE).append(";\n\n");

		//imports
		for(String imp : IMPORTS) {
			s.append(imp);
		}
		s.append("\n");
		
		parseFolder(s, root, 0);
	}
	
	private static void parseFolder(StringBuilder s, String fpath, int depth) {
		java.io.File file = new java.io.File(fpath);
		
		s.append(getIndent(depth));
		
		if(depth == 0) {
			s.append("public class ");
		} else {
			s.append("public static class ");
		}
		
		s.append(file.getName()).append("{\n");
		
		File[] files = file.listFiles();
		
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				if(o1.isDirectory() && !o2.isDirectory()) {
					return -1;
				} else if(!o1.isDirectory() && o2.isDirectory()) {
					return 1;
				} else {
					return o1.getName().compareTo(o2.getName());
				}
			}
		});
		
		for(int i = 0; i < files.length; i++) {
			java.io.File f = files[i];
			if(f.isDirectory()) {
				parseFolder(s, f.getPath(), depth + 1);
				if(i != files.length - 1) s.append("\n");
			} else {
				parseFile(s, f.getPath(), depth + 1);
			}
		}
		
		s.append(getIndent(depth)).append("}\n");
	}
	
	private static void parseFile(StringBuilder s, String fpath, int depth) {
		boolean handled = false;
		for(FileHandler handler : fileHandlers) {
			if(handler.canHandle(fpath)) {
				s.append(getIndent(depth)).append(handler.handle(fpath)).append("\n");
				handled |= true;
			}
		}
		if(!handled) {
			Log.error("Couldn't handle file: {}. Skipping.", fpath);
		}
	}
	
	private static abstract class FileHandler {
		
		public abstract boolean canHandle(String fpath);
		public abstract String handle(String fpath);
		
		protected String getExtension(String fpath) {
			return fpath.substring(fpath.lastIndexOf(".") + 1);
		}
		
		protected String getName(String fpath) {
			int sep1 = fpath.lastIndexOf("/");
			int sep2 = fpath.lastIndexOf("\\");
			return fpath.substring((sep1 > sep2 ? sep1 : sep2) + 1);
		}
		
		protected String sanitize(String name) {
			return name.replaceAll("-", "_");
		}
	}
	
	private static class TTFHandler extends FileHandler {

		@Override
		public boolean canHandle(String fpath) {
			return getExtension(fpath).equalsIgnoreCase("ttf");
		}

		@Override
		public String handle(String fpath) {
			String name = getName(fpath);
			name = name.substring(0, name.indexOf("."));
			return "public static final Font " + name + " = IO.loadFont(\"" + fpath + "\");"; 
		}
		
	}
	
	private static class SpriteHandler extends FileHandler {

		@Override
		public boolean canHandle(String fpath) {
			return getExtension(fpath).equalsIgnoreCase("sprite") || (getExtension(fpath).equalsIgnoreCase("png") && fpath.contains("sprites"));
		}

		@Override
		public String handle(String fpath) {
			String name = getName(fpath);
			name = name.substring(0, name.indexOf("."));
			return "public static final SpriteSheet " + name + " = IO.loadSpriteSheet(\"" + fpath + "\");"; 
		}
		
	}
	
	private static class PNGHandler extends FileHandler {

		@Override
		public boolean canHandle(String fpath) {
			return getExtension(fpath).equalsIgnoreCase("png") && !fpath.contains("sprites");
		}

		@Override
		public String handle(String fpath) {
			String name = getName(fpath);
			name = name.substring(0, name.indexOf("."));
			return "public static final PixelImage " + name + " = IO.loadImage(\"" + fpath + "\");"; 
		}
		
	}
	
	private static class WAVHandler extends FileHandler {

		@Override
		public boolean canHandle(String fpath) {
			return getExtension(fpath).equalsIgnoreCase("wav");
		}

		@Override
		public String handle(String fpath) {
			String name = getName(fpath);
			name = name.substring(0, name.indexOf("."));
			name = sanitize(name);
			return "public static final byte[] " + name + " = IO.loadAudio(\"" + fpath + "\");";
		}
		
	}
	
	private static class WAVClipHandler extends FileHandler {

		@Override
		public boolean canHandle(String fpath) {
			return getExtension(fpath).equalsIgnoreCase("wav");
		}

		@Override
		public String handle(String fpath) {
			String name = getName(fpath);
			name = name.substring(0, name.indexOf("."));
			name = sanitize(name);
			return "public static final Clip " + name + "_clip = IO.loadClip(\"" + fpath + "\");"; 
		}
		
	}
		
	public static void main(String[] args) {		
		StringBuilder builder = new StringBuilder();
		parse(builder, "R");
		IO.writeFile(builder.toString().replace(SEPERATOR, '/'), "src/resources/R.java");
	}

}
