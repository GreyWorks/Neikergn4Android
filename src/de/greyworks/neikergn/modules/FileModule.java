package de.greyworks.neikergn.modules;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
			Log.e(Statics.TAG, "SaveFile: File not found: " + path);
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(Statics.TAG, "SaveFile: IO Error");
			e.printStackTrace();
		}
	}

	/**
	 * load String from file in private mode
	 * @param ctx		context
	 * @param path		file path
	 * @return			content String
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
			Log.e(Statics.TAG, "LoadFile: IO Error");
			e.printStackTrace();
		}
		return content;
	}

}
