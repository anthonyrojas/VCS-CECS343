import java.util.*;
/**
 * @author Anthony Rojas 011819338
 * @version 3.0
 */
import java.io.*;
public class CreateRepo {
	private static boolean manifest_update_validation = true;
	//main method
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the name of the repo: ");
		String repo_name = in.next();
		Repo repo = new Repo("C:" + File.separator + repo_name);
		display_entry(repo, in);
		System.out.println("VCS terminated...");
	}//active lines: 6
	
	//display the entry instructions and prompt of the VCS
	public static void display_entry(Repo repo, Scanner in){
		System.out.print(repo.get_current_path() + "> Enter a command or help for a list of instructions: ");
		String command = in.next();
		while(!command.equalsIgnoreCase("exit")){
			menu(command, repo, in);
			System.out.print(repo.get_current_path() + "> Enter a command or help for a list of instructions: ");
			command = in.next();
		}
	}//active lines: 6
	
	//menu method
	public static void menu(String command, Repo repo, Scanner in){
		manifest_update_validation = true;
		if(command.equalsIgnoreCase("ls")){
			System.out.print(repo.get_current_path() + "\n");
			repo.display_local_files(repo.get_current_path(), "\t");
			manifest_update_validation = false;
		}
		else if(command.equalsIgnoreCase("mkdir")){
			repo.create_directory(in);
		}
		else  if(command.equalsIgnoreCase("cd")){
			repo.change_directory(in);
			manifest_update_validation = false;
		}
		else if(command.equalsIgnoreCase("rm")){
			repo.remove_directory(in);
		}
		else if(command.equalsIgnoreCase("mkf")){
			repo.create_file(in);
		}
		else if(command.equalsIgnoreCase("mv")){
			repo.move_directory(in);
		}
		else if(command.equalsIgnoreCase("co")){
			repo.check_out(in);
		}
		else if(command.equalsIgnoreCase("ci")){
			repo.check_in(in);
		}
		else if(command.equalsIgnoreCase("mrg")){
			repo.merge_project_tree(in);
		}
		else if(command.equalsIgnoreCase("help")){
			display_instructions();
			manifest_update_validation = false;
		}
		else{
			System.out.print(" Invalid input\n");
			manifest_update_validation = false;
		}
		call_update_manifest(repo);
	}//active lines: 25
	
	public static void call_update_manifest(Repo repo){
		if(manifest_update_validation)
			repo.update_manifest();
	}
	
	//display instructions for menu method
	public static void display_instructions(){
		String instructions = " ls - local files and folders/subfolders" 
				+ "\n mkdir - create a directory within the current folder"
				+ "\n cd - change current folder/directory" 
				+ "\n rm - remove a folder or file"
				+ "\n mkf - create a file"
				+ "\n mv - move a folder or file"
				+ "\n co - check out a file"
				+ "\n ci - check in a file"
				+ "\n mrg - merge files";
		System.out.println(instructions);
	}//active lines: 2
	
}
