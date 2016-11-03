package com.example.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Fileinfo;

import android.os.Handler;
import android.os.Message;

/**
 * 文件管理调用
 * **/
public class FileManager {
	// 定义的文件类型 不可更改
	public static final String TYPE_ANY = "all";
	public static final String TYPE_TXT = "txt";
	public static final String TYPE_IMAGE = "image";
	public static final String TYPE_VIDEO = "video";
	public static final String TYPE_AUDIO = "audio";
	public static final String TYPE_ZIP = "zip";
	public static final String TYPE_APK = "apk";

	static FileManager fm = null;

	// 单例
	public static FileManager getFileManager() {
		if (fm == null) {
			return fm = new FileManager();
		}
		return fm;
	}

	private Handler mhandler;

	public Handler getMhandler() {
		return mhandler;
	}

	public void setMhandler(Handler mhandler) {
		this.mhandler = mhandler;
	}

	// 路径
	public static String inPath = null;
	public static String outPath = null;
	// 上来就执行
	static {
		inPath = PhoneUtils.getBuiltinPath();// 内置
		outPath = PhoneUtils.getExternalPath();// 外置
	}
	// 所有文件
	public List<Fileinfo> Listall;
	// 所有文件大小
	public long ListallSize = 0;
	// 文档
	public List<Fileinfo> Listtext;
	// 文档大小
	public long ListtextSize = 0;
	// 视频
	public List<Fileinfo> Listvideo;
	// 视频大小
	public long ListvideoSize = 0;
	// 音频
	public List<Fileinfo> Listaudio;
	// 音频大小
	public long ListaudioSize = 0;
	// 图像
	public List<Fileinfo> Listimage;
	// 图像大小
	public long ListimageSize = 0;
	// 压缩包
	public List<Fileinfo> Listzip;
	// 压缩包大小
	public long ListzipSize = 0;
	// 程序包
	public List<Fileinfo> Listapk;
	// 程序包大小
	public long ListapkSize = 0;

	// 私有化构造 防止外部new
	private FileManager() {
		Listall = new ArrayList<Fileinfo>();
		Listtext = new ArrayList<Fileinfo>();
		Listvideo = new ArrayList<Fileinfo>();
		Listaudio = new ArrayList<Fileinfo>();
		Listimage = new ArrayList<Fileinfo>();
		Listzip = new ArrayList<Fileinfo>();
		Listapk = new ArrayList<Fileinfo>();
	}
	public boolean isloadingdata=false;//是否加载完大小
	// 查询操作 路径 是否执行完
	public void selectFile(String path, boolean isselect) {
		isloadingdata=false;
		if (path == null) {
			return;
		}
		File f = new File(path);
		if (f == null || !f.exists() || !f.canRead()) {
			isloadingdata=true;//当加载完true后变为完成
			mhandler.sendEmptyMessage(2);
			return;
		}//异常情况这里跳转
		// 目录 或者文件夹
		if (f.isDirectory()) {
			File[] listFiles = f.listFiles();// 文件夹下面所有文件
				for (int i = 0; i < listFiles.length; i++) {
					File file = listFiles[i];// 集合下标拿到文件
					if (file != null && file.exists() && file.canRead()
							&& file.getAbsolutePath() != null) {// file不空 并且存在
																// 并且可读
						selectFile(file.getAbsolutePath(), false);// 路径
																	// 后面执行false
					}
				}
		} else {// 是文件
			Fileinfo fileinfo = new Fileinfo();
			String name = f.getName();
			String filename = getFilename(name);
			if (filename == null) {
				return;
			}
			fileinfo.setFilename(filename);
			fileinfo.setF(f);
			for (int i = 0; i < ICON_TYPE_Table.length; i++) {
				if (filename.equals(ICON_TYPE_Table[i][0])) {
					fileinfo.setFileiocn(ICON_TYPE_Table[i][1]);// 图标的名字
					fileinfo.setFileType(ICON_TYPE_Table[i][2]);
				}
			}
			if (fileinfo.getFileiocn() == null
					|| fileinfo.getFileType() == null) {
				return;
			}
			if (fileinfo.getFileType().equals(FileManager.TYPE_TXT)) {
				Listtext.add(fileinfo);
				ListtextSize = ListtextSize + f.length();
			} else if (fileinfo.getFileType().equals(FileManager.TYPE_VIDEO)) {
				Listvideo.add(fileinfo);
				ListvideoSize = ListvideoSize + f.length();
			} else if (fileinfo.getFileType().equals(FileManager.TYPE_AUDIO)) {
				Listaudio.add(fileinfo);
				ListaudioSize = ListaudioSize + f.length();
			} else if (fileinfo.getFileType().equals(FileManager.TYPE_IMAGE)) {
				Listimage.add(fileinfo);
				ListimageSize = ListimageSize + f.length();
			} else if (fileinfo.getFileType().equals(FileManager.TYPE_ZIP)) {
				Listzip.add(fileinfo);
				ListzipSize = ListzipSize + f.length();
			} else if (fileinfo.getFileType().equals(FileManager.TYPE_APK)) {
				Listapk.add(fileinfo);
				ListapkSize = ListapkSize + f.length();
			}
			Listall.add(fileinfo);// 全部
			ListallSize = ListallSize + f.length();
			long length = f.length();
			Message message = mhandler.obtainMessage();
			message.what = 1;
			message.obj = fileinfo.getFileType();// 发消息
			mhandler.sendMessage(message);
		}
		if (isselect == true) {
			isloadingdata=true;//当加载完true后变为完成
			mhandler.sendEmptyMessage(2);
			
		}
	}

	// 文件的名字 xxx.mp4
	public String getFilename(String str) {
		int index = str.indexOf('.');// 找到点的位置
		if (index == -1) {
			return null;
		}
		return str.substring(index + 1, str.length());// +1开始截取名字
	}

	private static final String[][] ICON_TYPE_Table = {
			// {文件后缀名，文件对应图像名称,　文件所属类型}
			{ "apk", "icon_video", TYPE_APK },
			{ "3gp", "icon_video", TYPE_VIDEO },
			{ "aif", "icon_audio", TYPE_AUDIO },
			{ "aifc", "icon_audio", TYPE_AUDIO },
			{ "aiff", "icon_audio", TYPE_AUDIO },
			{ "als", "icon_audio", TYPE_AUDIO },
			{ "asc", "icon_text_plain", TYPE_TXT },
			{ "asf", "icon_video", TYPE_VIDEO },
			{ "asx", "icon_video", TYPE_VIDEO },
			{ "au", "icon_audio", TYPE_AUDIO },
			{ "avi", "icon_video", TYPE_VIDEO },
			{ "awb", "icon_audio", TYPE_AUDIO },
			{ "bmp", "icon_bmp", TYPE_IMAGE },
			{ "bz2", "icon_archive", TYPE_ZIP }, { "c", "icon_c", TYPE_TXT },
			{ "cpp", "icon_cpp", TYPE_TXT }, { "css", "icon_css", TYPE_TXT },
			{ "dhtml", "icon_html", TYPE_TXT },
			{ "doc", "icon_doc", TYPE_TXT }, { "dot", "icon_doc", TYPE_TXT },
			{ "es", "icon_audio", TYPE_AUDIO },
			{ "esl", "icon_audio", TYPE_AUDIO },
			{ "fvi", "icon_video", TYPE_VIDEO },
			{ "gif", "icon_gif", TYPE_IMAGE }, { "gz", "icon_gzip", TYPE_ZIP },
			{ "h", "icon_c_h", TYPE_TXT }, { "htm", "icon_html", TYPE_TXT },
			{ "html", "icon_html", TYPE_TXT },
			{ "hts", "icon_html", TYPE_TXT },
			{ "ico", "icon_ico", TYPE_IMAGE },
			{ "ief", "icon_image", TYPE_IMAGE },
			{ "ifm", "icon_gif", TYPE_IMAGE },
			{ "ifs", "icon_image", TYPE_IMAGE },
			{ "imy", "icon_audio", TYPE_AUDIO },
			{ "it", "icon_audio", TYPE_AUDIO },
			{ "itz", "icon_audio", TYPE_AUDIO },
			{ "j2k", "icon_jpeg", TYPE_IMAGE },
			{ "java", "icon_java", TYPE_TXT },
			{ "jar", "icon_java", TYPE_ZIP },
			{ "jnlp", "icon_java", TYPE_TXT },
			{ "jpe", "icon_jpeg", TYPE_IMAGE },
			{ "jpeg", "icon_jpeg", TYPE_IMAGE },
			{ "jpg", "icon_jpeg", TYPE_IMAGE },
			{ "jpz", "icon_jpeg", TYPE_IMAGE },
			{ "js", "icon_javascript", TYPE_TXT },
			{ "lsf", "icon_video", TYPE_VIDEO },
			{ "lsx", "icon_video", TYPE_VIDEO },
			{ "m15", "icon_audio", TYPE_AUDIO },
			{ "m3u", "icon_playlist", TYPE_AUDIO },
			{ "m3url", "icon_playlist", TYPE_AUDIO },
			{ "ma1", "icon_audio", TYPE_AUDIO },
			{ "ma2", "icon_audio", TYPE_AUDIO },
			{ "ma3", "icon_audio", TYPE_AUDIO },
			{ "ma5", "icon_audio", TYPE_AUDIO },
			{ "mdz", "icon_audio", TYPE_AUDIO },
			{ "mid", "icon_audio", TYPE_AUDIO },
			{ "midi", "icon_audio", TYPE_AUDIO },
			{ "mil", "icon_image", TYPE_IMAGE },
			{ "mio", "icon_audio", TYPE_AUDIO },
			{ "mng", "icon_video", TYPE_VIDEO },
			{ "mod", "icon_audio", TYPE_AUDIO },
			{ "mov", "icon_video", TYPE_VIDEO },
			{ "movie", "icon_video", TYPE_VIDEO },
			{ "mp2", "icon_mp3", TYPE_AUDIO },
			{ "mp3", "icon_mp3", TYPE_AUDIO },
			{ "mp4", "icon_video", TYPE_VIDEO },
			{ "mpe", "icon_video", TYPE_VIDEO },
			{ "mpeg", "icon_video", TYPE_VIDEO },
			{ "mpg", "icon_video", TYPE_VIDEO },
			{ "mpg4", "icon_video", TYPE_VIDEO },
			{ "mpga", "icon_mp3", TYPE_AUDIO },
			{ "nar", "icon_zip", TYPE_ZIP },
			{ "nbmp", "icon_image", TYPE_IMAGE },
			{ "nokia-op-logo", "icon_image", TYPE_IMAGE },
			{ "nsnd", "icon_audio", TYPE_AUDIO },
			{ "pac", "icon_audio", TYPE_AUDIO },
			{ "pae", "icon_audio", TYPE_AUDIO },
			{ "pbm", "icon_bmp", TYPE_IMAGE },
			{ "pcx", "icon_image", TYPE_IMAGE },
			{ "pda", "icon_image", TYPE_IMAGE },
			{ "pdf", "icon_pdf", TYPE_TXT },
			{ "pgm", "icon_image", TYPE_IMAGE },
			{ "pict", "icon_image", TYPE_IMAGE },
			{ "png", "icon_png", TYPE_IMAGE },
			{ "pnm", "icon_image", TYPE_IMAGE },
			{ "pnz", "icon_png", TYPE_IMAGE }, { "pot", "icon_ppt", TYPE_TXT },
			{ "ppm", "icon_image", TYPE_IMAGE },
			{ "pps", "icon_ppt", TYPE_TXT }, { "ppt", "icon_ppt", TYPE_TXT },
			{ "pvx", "icon_video", TYPE_VIDEO },
			{ "qcp", "icon_audio", TYPE_AUDIO },
			{ "qt", "icon_video", TYPE_VIDEO },
			{ "qti", "icon_image", TYPE_IMAGE },
			{ "qtif", "icon_image", TYPE_IMAGE },
			{ "ra", "icon_audio", TYPE_AUDIO },
			{ "ram", "icon_audio", TYPE_AUDIO },
			{ "rar", "icon_rar", TYPE_ZIP },
			{ "ras", "icon_image", TYPE_IMAGE },
			{ "rf", "icon_image", TYPE_IMAGE },
			{ "rgb", "icon_image", TYPE_IMAGE },
			{ "rm", "icon_audio", TYPE_AUDIO },
			{ "rmf", "icon_audio", TYPE_AUDIO },
			{ "rmm", "icon_audio", TYPE_AUDIO },
			{ "rmvb", "icon_audio", TYPE_AUDIO },
			{ "rp", "icon_image", TYPE_IMAGE },
			{ "rpm", "icon_audio", TYPE_AUDIO },
			{ "rtf", "icon_text_richtext", TYPE_TXT },
			{ "rv", "icon_video", TYPE_VIDEO },
			{ "s3m", "icon_audio", TYPE_AUDIO },
			{ "s3z", "icon_audio", TYPE_AUDIO },
			{ "shtml", "icon_html", TYPE_TXT },
			{ "si6", "icon_image", TYPE_IMAGE },
			{ "si7", "icon_image", TYPE_IMAGE },
			{ "si9", "icon_image", TYPE_IMAGE },
			{ "smd", "icon_audio", TYPE_AUDIO },
			{ "smz", "icon_audio", TYPE_AUDIO },
			{ "snd", "icon_audio", TYPE_AUDIO },
			{ "stm", "icon_audio", TYPE_AUDIO },
			{ "svf", "icon_image", TYPE_IMAGE },
			{ "svg", "icon_image", TYPE_IMAGE },
			{ "svh", "icon_image", TYPE_IMAGE },
			{ "swf", "icon_flash", TYPE_VIDEO },
			{ "swfl", "icon_flash", TYPE_VIDEO },
			{ "tar", "icon_tar", TYPE_ZIP }, { "taz", "icon_tar", TYPE_ZIP },
			{ "tgz", "icon_tar", TYPE_ZIP },
			{ "tif", "icon_tiff", TYPE_IMAGE },
			{ "tiff", "icon_tiff", TYPE_IMAGE },
			{ "toy", "icon_image", TYPE_IMAGE },
			{ "tsi", "icon_audio", TYPE_AUDIO },
			{ "txt", "icon_text_plain", TYPE_TXT },
			{ "ult", "icon_audio", TYPE_AUDIO },
			{ "vdo", "icon_video", TYPE_VIDEO },
			{ "vib", "icon_audio", TYPE_AUDIO },
			{ "viv", "icon_video", TYPE_VIDEO },
			{ "vivo", "icon_video", TYPE_VIDEO },
			{ "vox", "icon_audio", TYPE_AUDIO },
			{ "vqe", "icon_audio", TYPE_AUDIO },
			{ "vqf", "icon_audio", TYPE_AUDIO },
			{ "vql", "icon_audio", TYPE_AUDIO },
			{ "wav", "icon_wav", TYPE_VIDEO },
			{ "wax", "icon_audio", TYPE_AUDIO },
			{ "wbmp", "icon_bmp", TYPE_IMAGE },
			{ "wi", "icon_image", TYPE_IMAGE },
			{ "wm", "icon_video", TYPE_VIDEO },
			{ "wma", "icon_wma", TYPE_AUDIO },
			{ "wmv", "icon_video", TYPE_VIDEO },
			{ "wmx", "icon_video", TYPE_VIDEO },
			{ "wpng", "icon_png", TYPE_IMAGE },
			{ "wv", "icon_video", TYPE_VIDEO },
			{ "wvx", "icon_video", TYPE_VIDEO },
			{ "xbm", "icon_bmp", TYPE_IMAGE },
			{ "xht", "icon_xhtml_xml", TYPE_TXT },
			{ "xhtm", "icon_xhtml_xml", TYPE_TXT },
			{ "xhtml", "icon_xhtml_xml", TYPE_TXT },
			{ "xla", "icon_xls", TYPE_TXT }, { "xlc", "icon_xls", TYPE_TXT },
			{ "xlm", "icon_xls", TYPE_TXT }, { "xls", "icon_xls", TYPE_TXT },

			{ "xlt", "icon_xls", TYPE_TXT }, { "xlw", "icon_xls", TYPE_TXT },
			{ "xm", "icon_audio", TYPE_AUDIO },
			{ "xml", "icon_xml", TYPE_TXT },
			{ "xmz", "icon_audio", TYPE_AUDIO },
			{ "xpm", "icon_image", TYPE_IMAGE },
			{ "xsit", "icon_xml", TYPE_TXT }, { "xsl", "icon_xml", TYPE_TXT },
			{ "xwd", "icon_image", TYPE_IMAGE },
			{ "zip", "icon_zip", TYPE_ZIP } };

	/** 文件MIME类型(主要用做打开操作时，指定打开的指定文件对应所属的MIME类型) */
	public static final String[][] MIME_Table = {
			// {后缀名，MIME类型}
			{ "aab", "application/x-authoware-bin" },
			{ "aam", "application/x-authoware-map" },
			{ "aas", "application/x-authoware-seg" },
			{ "amc", "application/x-mpeg" },
			{ "ani", "application/octet-stream" },
			{ "apk", "application/vnd.android.package-archive" },
			{ "asd", "application/astound" }, { "asn", "application/astound" },
			{ "asp", "application/x-asap" },
			{ "ai", "application/postscript" },
			{ "avb", "application/octet-stream" },
			{ "bcpio", "application/x-bcpio" },
			{ "bin", "application/octet-stream" },
			{ "bld", "application/bld" }, { "bld2", "application/bld2" },
			{ "aif", "audio/x-aiff" }, { "aifc", "audio/x-aiff" },
			{ "aiff", "audio/x-aiff" }, { "als", "audio/X-Alpha5" },
			{ "au", "audio/basic" }, { "awb", "audio/amr-wb" },
			{ "3gp", "video/3gpp" }, { "asf", "video/x-ms-asf" },
			{ "asx", "video/x-ms-asf" }, { "avi", "video/x-msvideo" },
			{ "asc", "text/plain" }, { "bmp", "image/bmp" },
			{ "bpk", "application/octet-stream" },
			{ "bz2", "application/x-bzip2" }, { "c", "text/x-csrc" },
			{ "cpp", "text/x-c++src" }, { "cal", "image/x-cals" },
			{ "ccn", "application/x-cnc" }, { "cco", "application/x-cocoa" },
			{ "cdf", "application/x-netcdf" },
			{ "cgi", "magnus-internal/cgi" }, { "chat", "application/x-chat" },
			{ "class", "application/octet-stream" },
			{ "clp", "application/x-msclip" }, { "cmx", "application/x-cmx" },
			{ "co", "application/x-cult3d-object" },
			{ "cod", "image/cis-cod" }, { "csh", "application/x-csh" },
			{ "csm", "chemical/x-csml" }, { "csml", "chemical/x-csml" },
			{ "css", "text/css" }, { "dcm", "x-lml/x-evm" },
			{ "cpio", "application/x-cpio" },
			{ "cpt", "application/mac-compactpro" },
			{ "crd", "application/x-mscardfile" },
			{ "cur", "application/octet-stream" },
			{ "dcr", "application/x-director" },
			{ "dir", "application/x-director" },
			{ "dll", "application/octet-stream" },
			{ "dmg", "application/octet-stream" },
			{ "dms", "application/octet-stream" },
			{ "doc", "application/msword" }, { "dot", "application/x-dot" },
			{ "dvi", "application/x-dvi" }, { "dwg", "application/x-autocad" },
			{ "dxf", "application/x-autocad" },
			{ "dxr", "application/x-director" },
			{ "ebk", "application/x-expandedbook" },
			{ "etc", "application/x-earthtime" }, { "dcx", "image/x-dcx" },
			{ "dhtml", "text/html" }, { "dwf", "drawing/x-dwf" },
			{ "emb", "chemical/x-embl-dl-nucleotide" },
			{ "embl", "chemical/x-embl-dl-nucleotide" },
			{ "eps", "application/postscript" }, { "eri", "image/x-eri" },
			{ "es", "audio/echospeech" }, { "esl", "audio/echospeech" },
			{ "etx", "text/x-setext" }, { "evm", "x-lml/x-evm" },
			{ "evy", "application/x-envoy" },
			{ "exe", "application/octet-stream" },
			{ "fh4", "image/x-freehand" }, { "fh5", "image/x-freehand" },
			{ "fhc", "image/x-freehand" }, { "fif", "image/fif" },
			{ "fm", "application/x-maker" }, { "fpx", "image/x-fpx" },
			{ "fvi", "video/isivideo" },
			{ "gau", "chemical/x-gaussian-input" },
			{ "gca", "application/x-gca-compressed" },
			{ "gdb", "x-lml/x-gdb" }, { "gif", "image/gif" },
			{ "gps", "application/x-gps" }, { "gtar", "application/x-gtar" },
			{ "gz", "application/x-gzip" }, { "h", "text/x-chdr" },
			{ "hdf", "application/x-hdf" }, { "hdm", "text/x-hdml" },
			{ "hdml", "text/x-hdml" }, { "hlp", "application/winhlp" },
			{ "hqx", "application/mac-binhex40" }, { "htm", "text/html" },
			{ "html", "text/html" }, { "hts", "text/html" },
			{ "ice", "x-conference/x-cooltalk" },
			{ "ico", "application/octet-stream" }, { "ief", "image/ief" },
			{ "ifm", "image/gif" }, { "ifs", "image/ifs" },
			{ "imy", "audio/melody" }, { "ins", "application/x-NET-Install" },
			{ "ips", "application/x-ipscript" },
			{ "ipx", "application/x-ipix" }, { "it", "audio/x-mod" },
			{ "itz", "audio/x-mod" }, { "ivr", "i-world/i-vrml" },
			{ "j2k", "image/j2k" },
			{ "jad", "text/vnd.sun.j2me.app-descriptor" },
			{ "jam", "application/x-jam" }, { "java", "application/x-java" },
			{ "jar", "application/java-archive" },
			{ "jnlp", "application/x-java-jnlp-file" },
			{ "jpe", "image/jpeg" }, { "jpeg", "image/jpeg" },
			{ "jpg", "image/jpeg" }, { "jpz", "image/jpeg" },
			{ "js", "application/x-javascript" }, { "jwc", "application/jwc" },
			{ "kjx", "application/x-kjx" }, { "lak", "x-lml/x-lak" },
			{ "latex", "application/x-latex" },
			{ "lcc", "application/fastman" },
			{ "lcl", "application/x-digitalloca" },
			{ "lcr", "application/x-digitalloca" },
			{ "lgh", "application/lgh" },
			{ "lha", "application/octet-stream" }, { "lml", "x-lml/x-lml" },
			{ "lmlpack", "x-lml/x-lmlpack" }, { "lsf", "video/x-ms-asf" },
			{ "lsx", "video/x-ms-asf" }, { "lzh", "application/x-lzh" },
			{ "m13", "application/x-msmediaview" },
			{ "m14", "application/x-msmediaview" }, { "m15", "audio/x-mod" },
			{ "m3u", "audio/x-mpegurl" }, { "m3url", "audio/x-mpegurl" },
			{ "ma1", "audio/ma1" }, { "ma2", "audio/ma2" },
			{ "ma3", "audio/ma3" }, { "ma5", "audio/ma5" },
			{ "man", "application/x-troff-man" },
			{ "map", "magnus-internal/imagemap" },
			{ "mbd", "application/mbedlet" },
			{ "mct", "application/x-mascot" },
			{ "mdb", "application/x-msaccess" }, { "mdz", "audio/x-mod" },
			{ "me", "application/x-troff-me" }, { "mel", "text/x-vmel" },
			{ "mi", "application/x-mif" }, { "mid", "audio/midi" },
			{ "midi", "audio/midi" }, { "mif", "application/x-mif" },
			{ "mil", "image/x-cals" }, { "mio", "audio/x-mio" },
			{ "mmf", "application/x-skt-lbs" }, { "mng", "video/x-mng" },
			{ "mny", "application/x-msmoney" },
			{ "moc", "application/x-mocha" },
			{ "mocha", "application/x-mocha" }, { "mod", "audio/x-mod" },
			{ "mof", "application/x-yumekara" },
			{ "mol", "chemical/x-mdl-molfile" },
			{ "mop", "chemical/x-mopac-input" }, { "mov", "video/quicktime" },
			{ "movie", "video/x-sgi-movie" }, { "mp2", "audio/x-mpeg" },
			{ "mp3", "audio/x-mpeg" }, { "mp4", "video/mp4" },
			{ "mpc", "application/vnd.mpohun.certificate" },
			{ "mpe", "video/mpeg" }, { "mpeg", "video/mpeg" },
			{ "mpg", "video/mpeg" }, { "mpg4", "video/mp4" },
			{ "mpga", "audio/mpeg" },
			{ "mpn", "application/vnd.mophun.application" },
			{ "mpp", "application/vnd.ms-project" },
			{ "mps", "application/x-mapserver" }, { "mrl", "text/x-mrml" },
			{ "mrm", "application/x-mrm" }, { "ms", "application/x-troff-ms" },
			{ "mts", "application/metastream" },
			{ "mtx", "application/metastream" },
			{ "mtz", "application/metastream" },
			{ "mzv", "application/metastream" }, { "nar", "application/zip" },
			{ "nbmp", "image/nbmp" }, { "nc", "application/x-netcdf" },
			{ "ndb", "x-lml/x-ndb" }, { "ndwn", "application/ndwn" },
			{ "nif", "application/x-nif" }, { "nmz", "application/x-scream" },
			{ "nokia-op-logo", "image/vnd.nok-oplogo-color" },
			{ "npx", "application/x-netfpx" }, { "nsnd", "audio/nsnd" },
			{ "nva", "application/x-neva1" }, { "oda", "application/oda" },
			{ "oom", "application/x-AtlasMate-Plugin" },
			{ "pac", "audio/x-pac" }, { "pae", "audio/x-epac" },
			{ "pan", "application/x-pan" },
			{ "pbm", "image/x-portable-bitmap" }, { "pcx", "image/x-pcx" },
			{ "pda", "image/x-pda" }, { "pdb", "chemical/x-pdb" },
			{ "pdf", "application/pdf" }, { "pfr", "application/font-tdpfr" },
			{ "pgm", "image/x-portable-graymap" }, { "pict", "image/x-pict" },
			{ "pm", "application/x-perl" }, { "pmd", "application/x-pmd" },
			{ "png", "image/png" }, { "pnm", "image/x-portable-anymap" },
			{ "pnz", "image/png" }, { "pot", "application/vnd.ms-powerpoint" },
			{ "ppm", "image/x-portable-pixmap" },
			{ "pps", "application/vnd.ms-powerpoint" },
			{ "ppt", "application/vnd.ms-powerpoint" },
			{ "pqf", "application/x-cprplayer" },
			{ "pqi", "application/cprplayer" }, { "prc", "application/x-prc" },
			{ "proxy", "application/x-ns-proxy-autoconfig" },
			{ "ps", "application/postscript" },
			{ "ptlk", "application/listenup" },
			{ "pub", "application/x-mspublisher" },
			{ "pvx", "video/x-pv-pvx" }, { "qcp", "audio/vnd.qcelp" },
			{ "qt", "video/quicktime" }, { "qti", "image/x-quicktime" },
			{ "qtif", "image/x-quicktime" },
			{ "r3t", "text/vnd.rn-realtext3d" },
			{ "ra", "audio/x-pn-realaudio" },
			{ "ram", "audio/x-pn-realaudio" },
			{ "rar", "application/x-rar-compressed" },
			{ "ras", "image/x-cmu-raster" }, { "rdf", "application/rdf+xml" },
			{ "rf", "image/vnd.rn-realflash" }, { "rgb", "image/x-rgb" },
			{ "rlf", "application/x-richlink" },
			{ "rm", "audio/x-pn-realaudio" }, { "rmf", "audio/x-rmf" },
			{ "rmm", "audio/x-pn-realaudio" },
			{ "rmvb", "audio/x-pn-realaudio" },
			{ "rnx", "application/vnd.rn-realplayer" },
			{ "roff", "application/x-troff" },
			{ "rp", "image/vnd.rn-realpix" },
			{ "rpm", "audio/x-pn-realaudio-plugin" },
			{ "rt", "text/vnd.rn-realtext" }, { "rte", "x-lml/x-gps" },
			{ "rtf", "application/rtf" }, { "rtg", "application/metastream" },
			{ "rtx", "text/richtext" }, { "rv", "video/vnd.rn-realvideo" },
			{ "rwc", "application/x-rogerwilco" }, { "s3m", "audio/x-mod" },
			{ "s3z", "audio/x-mod" }, { "sca", "application/x-supercard" },
			{ "scd", "application/x-msschedule" },
			{ "sdf", "application/e-score" },
			{ "sea", "application/x-stuffit" }, { "sgm", "text/x-sgml" },
			{ "sgml", "text/x-sgml" }, { "sh", "application/x-sh" },
			{ "shar", "application/x-shar" },
			{ "shtml", "magnus-internal/parsed-html" },
			{ "shw", "application/presentations" }, { "si6", "image/si6" },
			{ "si7", "image/vnd.stiwap.sis" },
			{ "si9", "image/vnd.lgtwap.sis" },
			{ "sis", "application/vnd.symbian.install" },
			{ "sit", "application/x-stuffit" },
			{ "skd", "application/x-Koan" }, { "skm", "application/x-Koan" },
			{ "skp", "application/x-Koan" }, { "skt", "application/x-Koan" },
			{ "slc", "application/x-salsa" }, { "smd", "audio/x-smd" },
			{ "smi", "application/smil" }, { "smil", "application/smil" },
			{ "smp", "application/studiom" }, { "smz", "audio/x-smd" },
			{ "snd", "audio/basic" }, { "spc", "text/x-speech" },
			{ "spl", "application/futuresplash" },
			{ "spr", "application/x-sprite" },
			{ "sprite", "application/x-sprite" },
			{ "spt", "application/x-spt" },
			{ "src", "application/x-wais-source" },
			{ "stk", "application/hyperstudio" }, { "stm", "audio/x-mod" },
			{ "sv4cpio", "application/x-sv4cpio" },
			{ "sv4crc", "application/x-sv4crc" }, { "svf", "image/vnd" },
			{ "svg", "image/svg-xml" }, { "svh", "image/svh" },
			{ "svr", "x-world/x-svr" },
			{ "swf", "application/x-shockwave-flash" },
			{ "swfl", "application/x-shockwave-flash" },
			{ "t", "application/x-troff" },
			{ "tad", "application/octet-stream" }, { "talk", "text/x-speech" },
			{ "tar", "application/x-tar" }, { "taz", "application/x-tar" },
			{ "tbp", "application/x-timbuktu" },
			{ "tbt", "application/x-timbuktu" },
			{ "tcl", "application/x-tcl" }, { "tex", "application/x-tex" },
			{ "texi", "application/x-texinfo" },
			{ "texinfo", "application/x-texinfo" },
			{ "tgz", "application/x-tar" },
			{ "thm", "application/vnd.eri.thm" }, { "tif", "image/tiff" },
			{ "tiff", "image/tiff" }, { "tki", "application/x-tkined" },
			{ "tkined", "application/x-tkined" }, { "toc", "application/toc" },
			{ "toy", "image/toy" }, { "tr", "application/x-troff" },
			{ "trk", "x-lml/x-gps" }, { "trm", "application/x-msterminal" },
			{ "tsi", "audio/tsplayer" }, { "tsp", "application/dsptype" },
			{ "tsv", "text/tab-separated-values" },
			{ "tsv", "text/tab-separated-values" },
			{ "ttf", "application/octet-stream" },
			{ "ttz", "application/t-time" }, { "txt", "text/plain" },
			{ "ult", "audio/x-mod" }, { "ustar", "application/x-ustar" },
			{ "uu", "application/x-uuencode" },
			{ "uue", "application/x-uuencode" },
			{ "vcd", "application/x-cdlink" }, { "vcf", "text/x-vcard" },
			{ "vdo", "video/vdo" }, { "vib", "audio/vib" },
			{ "viv", "video/vivo" }, { "vivo", "video/vivo" },
			{ "vmd", "application/vocaltec-media-desc" },
			{ "vmf", "application/vocaltec-media-file" },
			{ "vmi", "application/x-dreamcast-vms-info" },
			{ "vms", "application/x-dreamcast-vms" },
			{ "vox", "audio/voxware" }, { "vqe", "audio/x-twinvq-plugin" },
			{ "vqf", "audio/x-twinvq" }, { "vql", "audio/x-twinvq" },
			{ "vre", "x-world/x-vream" }, { "vrml", "x-world/x-vrml" },
			{ "vrt", "x-world/x-vrt" }, { "vrw", "x-world/x-vream" },
			{ "vts", "workbook/formulaone" }, { "wav", "audio/x-wav" },
			{ "wax", "audio/x-ms-wax" }, { "wbmp", "image/vnd.wap.wbmp" },
			{ "web", "application/vnd.xara" }, { "wi", "image/wavelet" },
			{ "wis", "application/x-InstallShield" },
			{ "wm", "video/x-ms-wm" }, { "wma", "audio/x-ms-wma" },
			{ "wmd", "application/x-ms-wmd" },
			{ "wmf", "application/x-msmetafile" },
			{ "wml", "text/vnd.wap.wml" },
			{ "wmlc", "application/vnd.wap.wmlc" },
			{ "wmls", "text/vnd.wap.wmlscript" },
			{ "wmlsc", "application/vnd.wap.wmlscriptc" },
			{ "wmlscript", "text/vnd.wap.wmlscript" },
			{ "wmv", "audio/x-ms-wmv" }, { "wmx", "video/x-ms-wmx" },
			{ "wmz", "application/x-ms-wmz" }, { "wpng", "image/x-up-wpng" },
			{ "wpt", "x-lml/x-gps" }, { "wri", "application/x-mswrite" },
			{ "wrl", "x-world/x-vrml" }, { "wrz", "x-world/x-vrml" },
			{ "ws", "text/vnd.wap.wmlscript" },
			{ "wsc", "application/vnd.wap.wmlscriptc" },
			{ "wv", "video/wavelet" }, { "wvx", "video/x-ms-wvx" },
			{ "wxl", "application/x-wxl" }, { "x-gzip", "application/x-gzip" },
			{ "xar", "application/vnd.xara" }, { "xbm", "image/x-xbitmap" },
			{ "xdm", "application/x-xdma" }, { "xdma", "application/x-xdma" },
			{ "xdw", "application/vnd.fujixerox.docuworks" },
			{ "xht", "application/xhtml+xml" },
			{ "xhtm", "application/xhtml+xml" },
			{ "xhtml", "application/xhtml+xml" },
			{ "xla", "application/vnd.ms-excel" },
			{ "xlc", "application/vnd.ms-excel" },
			{ "xll", "application/x-excel" },
			{ "xlm", "application/vnd.ms-excel" },
			{ "xls", "application/vnd.ms-excel" },
			{ "xlt", "application/vnd.ms-excel" },
			{ "xlw", "application/vnd.ms-excel" }, { "xm", "audio/x-mod" },
			{ "xml", "text/xml" }, { "xmz", "audio/x-mod" },
			{ "xpi", "application/x-xpinstall" }, { "xpm", "image/x-xpixmap" },
			{ "xsit", "text/xml" }, { "xsl", "text/xml" },
			{ "xul", "text/xul" }, { "xwd", "image/x-xwindowdump" },
			{ "xyz", "chemical/x-pdb" }, { "yz1", "application/x-yz1" },
			{ "z", "application/x-compress" },
			{ "zac", "application/x-zaurus-zac" },
			{ "zip", "application/zip	" }, };
}
