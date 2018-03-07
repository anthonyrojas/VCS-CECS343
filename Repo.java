import java.awt.Desktop;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.nio.*;
import java.nio.file.*;
import java.text.*;
/**
 * @author Anthony Rojas 011819338
 * @version 3.0
 */
public class Repo {
	private String repo;
	private String current_directory;
	
	//Repo constructor
	public Repo(String r){
		repo = r;
		File d = new File(repo);
		d.mkdirs();
		current_directory = repo;
		implement_manifest();
		update_manifest();
	}//active lines: 6
	
	//create a new directory
	public void create_directory(Scanner in){
		System.out.print(current_directory + "> Enter the name of the new folder: ");
		String folder = in.next();
		folder = current_directory + File.separator + folder;
		File d = new File(folder);
		d.mkdir();
	}//active lines: 5
	
	//change the current directory
	public void change_directory(Scanner in){
		System.out.print(current_directory + "> Enter the name of directory you want or enter '^' to go up to the parent folder: ");
		String update_path = in.next();
		if(update_path.equalsIgnoreCase("^")){
			change_directory_to_parent();
		}else{
			change_directory_child(update_path);
		}
	}//active lines: 5
	
	//change the directory to a child of the current directory
	public void change_directory_child(String update_path){
		File d = new File(current_directory + File.separator + update_path);
		if(d.exists()){
			current_directory = current_directory + File.separator + update_path;
		}else{
			System.out.println(" directory does not exist");
		}
	}//active lines: 4
	
	//change the directory to the parent of the current directory
	public void change_directory_to_parent(){
		int truncate_index = current_directory.length();
		if(current_directory.equalsIgnoreCase("C:" + File.separator + repo)){
			System.out.print(" cannot exit repo\n");
		}else{
			for(int i = current_directory.length()-1; i >= 0; i--){
				if(File.separatorChar == current_directory.charAt(i)){
					truncate_index = i;
					break;
				}
			}
		}
		current_directory = current_directory.substring(0, truncate_index);
	}//active lines: 7
	
	//remove a directory or file
	public void remove_directory(Scanner in){
		System.out.print(current_directory + "> Enter the local directory you would like to delete: ");
		String d = in.next();
		File dir = new File(current_directory + File.separator + d);
		if(dir.exists()){
			try{
				Files.delete(dir.toPath());
			}
			catch(IOException e){
				System.out.println(" Could not delete.");
			}
		}
	}//active lines: 8
	
	//move a directory or file
	public void move_directory(Scanner in){
		System.out.print(repo + "> Enter the source folder: " );
		String s = in.next();
		System.out.print(repo + "> Enter the target folder: " );
		String t = in.next();
		File source = new File(repo + File.separator + s);
		File target = new File(repo + File.separator + t);
		move(source, target, s, t);
	}
	//active lines: 7
	
	//move the files or folder
	public void move(File source, File target, String s, String t){
		File [] paths = source.listFiles();
		for(File p : paths){
			String pStr = p.toString();
			if(pStr.contains(source.toString()))
				pStr.replace(source.toString(), "");
			File dir = new File(repo + File.separator + t + File.separator + pStr );
			if(!p.isFile()){
				move_is_not_a_file(dir, source, target);
			}
			else{
				move_is_a_file(dir, source, target);
			}	
		}
	}//active lines: 9
	
	//move a folder
	public void move_is_not_a_file(File dir, File source, File target){
		dir.mkdirs();
	}//active lines: 1
	
	//move a file
	public void move_is_a_file(File dir, File source, File target){
		Path src = source.toPath();
		Path trgt = target.toPath();
		CopyOption c = StandardCopyOption.REPLACE_EXISTING;
		try {
			Files.move(src, trgt, c);
		} catch (Exception e) {
			System.out.println(" could not move");
		}
	}//active lines: 7 
	
	//display all files amd folders within the current directory
	public void display_local_files(String dir, String spacing){
		File paths[];
		File d = new File(dir);
		paths = d.listFiles();
		for(File path: paths){
			String p = path.toString();
			int index = 0;
			String output = "";
			for(int i = p.length()-1; i >= 0; i--){
				char c = p.charAt(i);
				if(File.separatorChar == c){
					index = i;
					for(int j = index; j < p.length(); j++){
						output += p.charAt(j);
					}
					i = -1;
					break;
				}
			}
			if(path.isFile()){
				String timeStamp = get_time_stamp(path);
				System.out.println(spacing + output + " " + timeStamp);
			}else{
				System.out.println(spacing + output);
			}
			if(!path.isFile()){
				if( has_more_files(path.toString()) ){
					display_local_files(path.toString(), spacing + "\t" );
				}
			}
			
		}
	}//active lines: 23
	
	//check if a directory has more folders or files
	public boolean has_more_files(String dir){
		File d = new File(dir);
		if(d.isFile()){
			return false;
		}
		File paths[];
		paths = d.listFiles();
		if(paths.length > 0){
			return true;
		}else{
			return false;
		}
	}//active lines: 8
	
	//get timestamp of a file
	public String get_time_stamp(File dir){
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String time_stamp = df.format(dir.lastModified());
		return time_stamp;
	}//active lines: 3
	
	//get the name of a file
	public String get_filename(File path){
		int end = path.toString().length();
		int start = 0;
		for(int i = path.toString().length()-1; i >= 0; i--){
			if('.' == path.toString().charAt(i)){
				end = i;
			}
			if(File.separatorChar == path.toString().charAt(i)){
				start = i+1;
				break;
			}
		}
		return path.toString().substring(start, end);
	}//active lines: 7
	
	//calculate the checksum of a file
	public int calculate_checksum(File path){
		int sum = 0;
		int checksum;
		try {
			byte [] data = Files.readAllBytes(path.toPath());
			for(byte d : data){
				sum += (int)d;
			}
		} catch (IOException e) {
			System.out.println(" could not calculate checksum");
		}
		checksum = sum % 256;
		return checksum;
	}//active lines: 8
	
	//rename the file to its Artifact ID
	public void rename_file(String leaf_dir, File f){
		int artifact_id = calculate_checksum(f);
		String extension = get_file_extension(f);
		System.out.println(" New file name: " + leaf_dir + File.separator + Integer.toString(artifact_id) + extension);
		f.renameTo(new File(leaf_dir + File.separator + Integer.toString(artifact_id) + extension));
	}
	//active lines: 5
	
	//get the path of a file
	public String get_path(File f){
		int cutoff = 0;
		String temp = f.toString();
		for(int i = temp.length()-1; i > 0; i--){
			if(File.separatorChar == temp.charAt(i) || '/' == temp.charAt(i)){
				cutoff = i;
				String path = temp.substring(0, cutoff);
				return path;
			}
			
		}
		return "";
	}
	//active lines: 8
	
	//get the path of the current directory
	public String get_current_path(){
		return current_directory;
	}//active lines: 1
	
	//create a file
	public void create_file(Scanner in){
		System.out.print(current_directory + "> Enter the filename with extension: ");
		String filename = in.next();
		create_leaf_folder(filename);
	}//active lines: 9
	
	//create a leaf folder
	public void create_leaf_folder(String filename){
		File d = new File(current_directory + "\\" + filename);
		d.mkdirs();
		create_leaf_file(d.toString(), filename);
	}
	//active lines: 3
	
	//create a leaf folder artifact file
	public void create_leaf_file(String leaf_dir, String filename){
		File f = new File(leaf_dir + File.separator + filename);
		if(Desktop.isDesktopSupported()){
			edit_leaf_file(f);
		}
		rename_file(leaf_dir, f);
	}
	//active lines: 4
	
	//edit a file in a leaf folder
	public void edit_leaf_file(File f){
		try{
			PrintWriter w = new PrintWriter(f);
			w.close();
			Runtime r = Runtime.getRuntime();
			Process p = r.exec("Notepad " + f.toString());
			p.waitFor();
			p.destroy();
			r.freeMemory();
		}catch(IOException e){
			System.out.print(" Could not create file\n");
		}catch(Exception e){
			System.out.print(" Could not open file\n");
		}
	}
	//active lines: 10
	
	//check out a file
	public void check_out(Scanner in){
		System.out.print(current_directory + "> Enter the local file you want to check out: ");
		String filename = in.next();
		System.out.print(current_directory + "> Enter the check out location with its path: ");
		String checkout_path = in.next();
		File f = new File(current_directory + File.separator + filename);
		rename_for_check_out(f, filename, checkout_path);
	}//active lines: 6
	
	//get the real name of a file for checking out
	public void rename_for_check_out(File f, String filename, String checkout_path){
		String path = get_path(f);
		String real_filename = truncate_for_check_out(f);
		CopyOption c_op = StandardCopyOption.COPY_ATTRIBUTES;
		try {
			Files.copy(f.toPath(), (new File(checkout_path + File.separator + real_filename)).toPath(), c_op);
		} catch (IOException e) {
			System.out.print(" Could not copy\n");
		}
	}
	//active lines: 6
	
	//truncate a file for checking out
	public String truncate_for_check_out(File f){
		String fn = f.toString();
		int cutoff = 0;
		for(int i = fn.length()-1; i >= 0; i--){
			if(File.separatorChar == fn.charAt(i)){
				cutoff = i;
				break;
			}
		}
		fn = fn.substring(0, cutoff);
		File f2 = new File(fn);
		return (get_filename(f2) + get_file_extension(f2));
	}
	//active lines: 8
	
	//check in a file or folder
	public void check_in(Scanner in){
		System.out.print(current_directory + "> Enter the file and its path you want to check in: ");
		String filepath = in.next();
		System.out.print(current_directory + "> Enter the target folder within the current directory or [curr] for the current directory: ");
		String target = in.next();
		if(target.equalsIgnoreCase("[curr]"))
			copy_check_in_file(filepath, current_directory);
		else
			copy_check_in_file(filepath, current_directory + File.separator + target);		
	}//active lines: 7
	
	//copy a file to be checked into a specified folder
	public void copy_check_in_file(String filepath, String target){
		String file = get_filename(new File(filepath)) + get_file_extension(new File(filepath));
		File d = new File(target + File.separator + file);
		d.mkdirs();
		String f = rename_for_check_in(filepath);
		try {
			Files.copy((new File(filepath)).toPath(), new File(d.toString() + File.separator + f).toPath(), StandardCopyOption.COPY_ATTRIBUTES);
		} catch (Exception e) {
			System.out.print(" Could not check in\n");
		}
	}
	//active lines: 9
	
	//rename a file for checking into the repository
	public String rename_for_check_in(String filepath){
		File f = new File(filepath);
		String artifact_id = Integer.toString(calculate_checksum(f));
		String extension = get_file_extension(f);
		return (artifact_id + extension);
	}
	//active lines: 4
	
	//get the file extension of a file
	public String get_file_extension(File f){
		String filename = f.toString();
		int truncate_index = filename.length();
		for(int i = filename.length()-1; i >= 0; i--){
			if('.' == filename.charAt(i)){
				truncate_index = i;
				break;
			}
		}
		return filename.substring(truncate_index, filename.length());
	}//active lines: 7
	
	//create manifests folder within the repo
	public void implement_manifest(){
		File f = new File(repo + File.separator + "manifests");
		f.mkdirs();
	}
	//active lines: 2
	
	//updates the manifest folder
	public void update_manifest(){
		File r = new File(repo);
		String filename = get_time_stamp(r);
		filename = get_manifest_filename(filename);
		call_write_to_manifest(filename, r);
	}
	//active lines: 4
	
	//calls the method to write to a manifest
	public void call_write_to_manifest(String filename, File r){
		File paths[] = r.listFiles();
		try {
			File m = new File(repo + File.separator + "manifests" + File.separator + filename);
			PrintWriter w = new PrintWriter(m);
			w.println(get_time_stamp(m));
			write_manifests_to_file(paths, r.toString(), w);
			w.close();
		} catch (FileNotFoundException e) {
			System.out.println(" Could not write to manifest.");
		}
	}
	//active lines: 8
	
	//adjust the filename of the manifest file
	public String get_manifest_filename(String filename){
		filename = filename.replaceAll("/", "-");
		filename = filename.replaceAll(" ", "_Time-");
		filename = filename.replaceAll(":", "-");
		String extension = ".txt";
		return (filename + extension);
	}
	//active lines: 5
	
	//writes a manifest file
	public void write_manifests_to_file(File paths[], String dir, PrintWriter w){
		for(File p: paths){
			if(has_more_files(p.toString()))
				write_manifests_to_file(p.listFiles(), p.toString(), w);
			else{
				if(p.isFile())
					w.println(File.separator + p.toString() + " " + get_time_stamp(p));
				else
					w.println(File.separator + p.toString());
			}
		}
	}
	//active lines: 7
	
	//method to merge project trees or files
	public void merge_project_tree(Scanner in){
		System.out.print(current_directory + "> Enter the path of the external target project tree file: ");
		String target = in.next();
		System.out.print(current_directory + "> Enter the repository project tree file: ");
		String repo_file = in.next();
		File t = new File(target);
		File rf = new File(repo_file);
		apend_files(t, rf);
	}
	//active lines: 7
	
	//apend file names
	public void apend_files(File t, File rf){
		String target_path = get_path(t);
		System.out.println("Path: " + target_path);
		System.out.println("Filename repo: " + get_filename(rf) + "_MR");
		String t_fn = get_filename(t) + "_MT";
		String t_ext = get_file_extension(t);
		String rf_fn = get_filename(rf) + "_MR";
		String rf_ext = get_file_extension(rf);
		File t2 = new File(target_path + File.separator + t_fn + t_ext);
		File rf2 = new File(target_path + File.separator + rf_fn + rf_ext);
		t.renameTo(t2);
		rf.renameTo(rf2);
		merge_files(t2, rf2, target_path);
	}
	//active lines: 12
	
	//merges files when aftifact ID's are the same or when a grandpa file does not exist
	public void merge_files(File t, File rf, String target_path){
		String gp_file = find_grandpa_file(t, rf, target_path);
		if(!gp_file.isEmpty()){
			merge_files_manually(t, rf, new File(gp_file), target_path);
		}
		else if(calculate_checksum(t) == calculate_checksum(rf)){
			try {
				Files.copy(rf.toPath(), t.toPath(), StandardCopyOption.REPLACE_EXISTING);
				rename_file(target_path, t);
				rf.delete();
			} catch (IOException e) {
				System.out.println(" Could not copy files");
			}
		}
		else{
			merge_without_grandpa(t, rf, target_path);
		}
		remove_files_from_merge(target_path);
	}
	//active lines: 11
	
	//merge two files that are not alike and without a grandpa file
	public void merge_without_grandpa(File t, File rf, String target_path){
		try {
			File ofile = new File(target_path + File.separator + "merge.txt");
			FileOutputStream fos;
			FileInputStream fis = null;
			byte[] file_bytes = null;
			int bytes_read = 0;
			ArrayList<File> files = new ArrayList<File>();
			files.add(rf);
			files.add(t);
			fos = new FileOutputStream(ofile, true);
			merge_files_without_grandpa(files, fos, fis , file_bytes, bytes_read);
			fos.close();
			fos = null;
			rename_file(target_path, ofile);
		} catch (Exception e) {
			System.out.println(" unable to merge");
		}
	}
	//active lines: 14
	
	//merge contents of files when merging without grandpa
	public void merge_files_without_grandpa(ArrayList<File> files, FileOutputStream fos, FileInputStream fis, byte[] file_bytes, int bytes_read){
		try{
			for(File f: files){
				fis = new FileInputStream(f);
				file_bytes = new byte[(int)f.length()];
				bytes_read = fis.read(file_bytes, 0, (int)f.length());
				assert(bytes_read == file_bytes.length);
				assert(bytes_read == (int) f.length());
				fos.write(file_bytes);
				fos.flush();
				file_bytes = null;
				fis.close();
				fis=null;
			}
		}catch(Exception e){
			System.out.println(" unable merge");
		}
	}
	//active lines: 13
	
	//method to find grandpa file in the target tree
	public String find_grandpa_file(File t, File rf, String target_path){
		File tp = new File(target_path);
		tp.mkdirs();
		int newest = -1000;
		File files[] = tp.listFiles();
		if(files.length > 1){
			for(int i = 0; i < files.length; i++){
				if(files[i].lastModified() > newest){
					newest = i;
				}
			}
			return (files[newest].toString());
		}else{
			return "";
		}
	}
	//active lines: 11
	
	//merge files manually using a text editor when there are 3 files
	public void merge_files_manually(File mt, File mr, File mg, String target_path){
		try {
			File ofile = new File(target_path + File.separator + "merge.txt");
			FileOutputStream fos;
			FileInputStream fis = null;
			byte[] file_bytes = null;
			int bytes_read = 0;
			ArrayList<File> files = new ArrayList<File>();
			files.add(mt);
			files.add(mr);
			files.add(mg);
			fos = new FileOutputStream(ofile, true);
			merge_three_files(files, fos, fis , file_bytes, bytes_read);
			fos.close();
			fos = null;
			rename_file(target_path, ofile);
		} catch (Exception e) {
			System.out.println(" unable to merge");
		}
	}
	//active lines: 11
	
	public void merge_three_files(ArrayList<File> files, FileOutputStream fos, FileInputStream fis, byte[] file_bytes, int bytes_read){
		try{
			for(File f: files){
				fis = new FileInputStream(f);
				file_bytes = new byte[(int)f.length()];
				bytes_read = fis.read(file_bytes, 0, (int)f.length());
				assert(bytes_read == file_bytes.length);
				assert(bytes_read == (int) f.length());
				fos.write(file_bytes);
				fos.flush();
				file_bytes = null;
				fis.close();
				fis=null;
			}
		}catch(Exception e){
			System.out.println(" unable to merge");
		}
	}
	
	//rename the merged file
	public void rename_merged_files(File mt, File mr, File mg, String target_path){
		mt.delete();
		mr.delete();
		rename_file(target_path, mg);
	}
	//active lines: 3
	
	//delete the files used to merge
	public void remove_files_from_merge(String target_path){
		File d = new File(target_path);
		for(File f: d.listFiles()){
			if(get_filename(f).contains("MR") || get_filename(f).contains("MG") || get_filename(f).contains("MT")){
				f.delete();
			}
		}
	}
	//active lines: 4
	
}