package de.greyworks.neikergn;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import de.greyworks.neikergn.modules.MessageModule;
import de.greyworks.neikergn.modules.NewsModule;
import de.greyworks.neikergn.modules.NiBModule;
import de.greyworks.neikergn.modules.TerminModule;
import android.content.Context;
import android.widget.Toast;

public class Statics {
	public static String TAG = "Neikergn";
	public static Context ctx;
	public static OverviewActivity ovr;
	public static File extStor;
	
	public static final boolean VERBOSE = false;
	public static final boolean DEBUG = true;
	public static final boolean WARN = false;
	public static final boolean ERROR = true;
	
	public static SimpleDateFormat dateFormatIn = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);
	public static SimpleDateFormat dateFormatInShort = new SimpleDateFormat("yyyyMMdd", Locale.GERMANY);
	public static SimpleDateFormat dateFormatOut = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
	public static SimpleDateFormat dateFormatInFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
	public static SimpleDateFormat dateFormatOutFull = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);
	
	public static NewsModule newsModule;
	public static MessageModule messageModule;
	public static TerminModule terminModule;
	public static NiBModule nibModule;
	
	
	public static void showToast(String text) {
		Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
	}
	
	
	


}
