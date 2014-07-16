package de.greyworks.neikergn.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import android.content.Context;
import android.util.Log;
import de.greyworks.neikergn.Statics;

public class FileModule {

	/**
	 * save String to file in private mode
	 * 
	 * @param ctx
	 *            context
	 * @param content
	 *            file content to write
	 * @param path
	 *            file path
	 */
	public static void saveFile(Context ctx, String content, String path) {
		FileOutputStream fOut;
		try {
			fOut = ctx.openFileOutput(path, Context.MODE_PRIVATE);
			fOut.write(content.getBytes());
			fOut.close();
		} catch (FileNotFoundException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "SaveFile: File not found: " + path);
			e.printStackTrace();
		} catch (IOException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "SaveFile: IO Error");
			e.printStackTrace();
		}
	}

	/**
	 * load String from file in private mode
	 * 
	 * @param ctx
	 *            context
	 * @param path
	 *            file path
	 * @return content String
	 */
	public static String loadFile(Context ctx, String path) {
		String content = "";
		FileInputStream fIn = null;
		try {
			fIn = ctx.openFileInput(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fIn, "utf8"));
			StringBuilder sb = new StringBuilder();
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			content = sb.toString();
		} catch (IOException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "LoadFile: IO Error");
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * save String to file in private mode
	 * 
	 * @param ctx
	 *            context
	 * @param content
	 *            file content to write
	 * @param path
	 *            file path
	 */
	public static void saveFileBytes(Context ctx, byte[] content, String path) {
		FileOutputStream fOut;
		try {
			fOut = ctx.openFileOutput(path, Context.MODE_PRIVATE);
			fOut.write(content);
			fOut.close();
		} catch (FileNotFoundException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "SaveFile: File not found: " + path);
			e.printStackTrace();
		} catch (IOException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "SaveFile: IO Error");
			e.printStackTrace();
		}
	}

	/**
	 * load String from file in private mode
	 * 
	 * @param ctx
	 *            context
	 * @param path
	 *            file path
	 * @return content String
	 */
	public static byte[] loadFileBytes(Context ctx, String path) {
		byte[] content = null;
		File f = new File(Statics.ctx.getFilesDir().getAbsolutePath() + "/"
				+ path);
		if (f.exists()) {
			RandomAccessFile fRand = null;
			try {
				fRand = new RandomAccessFile(f, "r");
				long longlength = fRand.length();
	            int length = (int) longlength;
				content = new byte[length];
				fRand.readFully(content);


			
			} catch (IOException e) {
				if (Statics.ERROR)
					Log.e(Statics.TAG, "LoadFile: IO Error");
				e.printStackTrace();
			} finally {
				try {
					fRand.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return content;
		} else
			return null;
	}

	/**
	 * Deletes all json files stored by the app
	 * 
	 * @param ctx
	 *            context
	 */
	public static void deleteSavedData(Context ctx) {
		removeAllFilesIn(ctx.getFilesDir(), ctx);
		// remove all module entries to force complete reload
		Statics.newsModule.getItems().clear();
		Statics.newsModule.forceUpdateWeb();
		Statics.messageModule.getItems().clear();
		Statics.messageModule.forceUpdateWeb();
		Statics.nibModule.getItems().clear();
		Statics.nibModule.forceUpdateWeb();
		Statics.terminModule.getItems().clear();
		Statics.terminModule.forceUpdateWeb();
		Statics.showToast("Daten gelöscht. Aktualisierung gestartet.");

	}

	/**
	 * removes all downloads such as abfallkalender and mitteilungsblatt
	 * 
	 * @param ctx
	 */
	public static void deleteExternalFiles(Context ctx) {
		removeAllFilesIn(ctx.getExternalFilesDir(null), ctx);
		File dirMB = new File(ctx.getExternalFilesDir(null).getAbsolutePath()
				+ "/mitteilungsblatt");
		if (dirMB.isDirectory()) {
			removeAllFilesIn(dirMB, ctx);
		}
		Statics.showToast("Downloads gelöscht");
	}

	/**
	 * Service method for delete functions
	 * 
	 * @param dir
	 *            target directory
	 * @param ctx
	 *            context
	 */
	private static void removeAllFilesIn(File dir, Context ctx) {
		File files[] = dir.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				if (Statics.DEBUG)
					Log.d(Statics.TAG, "Deleting file: " + f.getAbsolutePath());
				f.delete();
			}
		}
	}

}
