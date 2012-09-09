package de.cygn.foobar2000;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 *
 * @author Thomas Friedel
 */
public class Playlists {

	/**
	 * This reads foobar2000\playlists\index.dat and if found, the playlist
	 * organizer config files. Returns a TreeModel which contains a list/tree of
	 * playlists, sorted into folders.
	 *
	 * @param playlistFolder
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static TreeModel readPlaylists(File foobarFolder) {
		FileInputStream f = null;
		TreeModel treeModel = new DefaultTreeModel(new DefaultMutableTreeNode());
		try {
			File playlistFolder = new File(foobarFolder, "playlists");
			f = new FileInputStream(new File(playlistFolder, "index.dat"));
			FileChannel ch = f.getChannel();
			MappedByteBuffer mb = ch.map(FileChannel.MapMode.READ_ONLY, 0L, ch.size());
			mb.order(ByteOrder.LITTLE_ENDIAN);
			int[] magic = new int[4];
			for (int i = 0; i < 4; i++) {
				magic[i] = mb.getInt();
			}
			int numberOfPlaylists = mb.getInt();
			ArrayList<Playlist> playlists = new ArrayList<>(numberOfPlaylists);
			int unknown1 = mb.getInt(); // 171
			for (int i = 0; i < numberOfPlaylists; i++) {
				Playlist playlist = new Playlist();
				playlist.filename = new File(playlistFolder, Utils.getString(mb)).toString();
				playlist.title = Utils.getString(mb);
				int unknown9 = mb.getInt(); // 1736
				int sizeOfRecord = mb.getInt(); // 40 usually. skip this many bytes
				// to get to next playlist
				int playlistType = mb.getInt(); // 1 for normal playlists, 2 for autoplaylists
				if (playlistType == 2) {
					playlist.type = Playlist.PlaylistType.AUTO;
				}
				// autoplaylists have a query pattern string, a sort pattern string and a 
				// force-sorted flag
				playlists.add(playlist);
				mb.position(mb.position() + sizeOfRecord - 4);
			}

			String treeString = readTreeString(foobarFolder);
			treeModel = parseTreeString(treeString, playlists);
		} catch (IOException ex) {
			Logger.getLogger(Playlists.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				f.close();
			} catch (IOException ex) {
				Logger.getLogger(Playlists.class.getName()).log(Level.SEVERE, null, ex);
			}
			return treeModel;
		}
	}

	public static TreeModel parseTreeString(String treeString, ArrayList<Playlist> playlists) {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Playlists");
		if (treeString.equals("")) {
			// no treeString given, just take a flat list as the "tree"
			for (Playlist playlist : playlists) {
				DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(playlist);
				top.add(new_node);
			}
		} else {
			// parse tree String
			int i = 0;
			ArrayList<DefaultMutableTreeNode> stack = new ArrayList<DefaultMutableTreeNode>();
			stack.add(top);
			DefaultMutableTreeNode current_node = stack.get(stack.size() - 1);
			while (i < treeString.length()) {
				// parse a line in the treeString which looks like this
				//  <P>72-good vibes
				//     or 
				//  <F> foldername
				//     or
				//  </F>
				StringBuilder sb = new StringBuilder();
				assert treeString.charAt(i) == '<';
				if (treeString.charAt(i + 1) == 'F') {
					// folder
					i += 5;
					while (treeString.charAt(i) != 10) {
						sb.append(treeString.charAt(i));
						i++;
					}
					i++;
					String foldername = sb.toString();
					DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(foldername);
					stack.add(new_node);
					current_node.add(new_node);
					current_node = new_node;
				} else if (treeString.charAt(i + 1) == 'P') {
					// playlist
					i += 3;
					while (treeString.charAt(i) != '-') {
						sb.append(treeString.charAt(i));
						i++;
					}
					int playlistNr = Integer.valueOf(sb.toString());
					while (treeString.charAt(i) != 10) {
						i++;
					}
					i++;
					Playlist playlist = playlists.get(playlistNr);
					DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(playlist);
					current_node.add(new_node);
				} else if (treeString.charAt(i + 1) == '/'
						&& treeString.charAt(i + 2) == 'F') {
					// end of folder
					i += 5;
					stack.remove(stack.size() - 1);
					current_node = stack.get(stack.size() - 1);
				}
			}
		}
		return new DefaultTreeModel(top);
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		TreeModel playlistTree = readPlaylists(new File("c:\\Users\\Thomas\\PortableApps\\foobar2000\\"));
	}

	/**
	 * opens playlist organizer configuration files and reads a string
	 * which stores the playlist tree. 
	 * @param foobarFolder
	 * @return
	 * @throws IOException 
	 */
	private static String readTreeString(File foobarFolder) throws IOException {
		FileInputStream plorgcfg = null;
		FileInputStream columnscfg = null;
		try {
			plorgcfg = new FileInputStream(new File(new File(foobarFolder, "configuration"), "foo_plorg.dll.cfg"));
		} catch (FileNotFoundException e) {
		}
		try {
			columnscfg = new FileInputStream(new File(new File(foobarFolder, "configuration"), "foo_ui_columns.dll.cfg"));
		} catch (FileNotFoundException e) {
		}
		String treeString = "";
		FileChannel ch;
		MappedByteBuffer mb;
		if (plorgcfg != null) {
			if (columnscfg != null) {
			// open columns file, find position of treestring, read string, return
				ch = columnscfg.getChannel();
				mb = ch.map(FileChannel.MapMode.READ_ONLY, 0L, ch.size());
				mb.order(ByteOrder.LITTLE_ENDIAN);
				while (mb.remaining() > 12) {
					long w = mb.getLong();
					// we are looking for at least 8 bytes of 0xFF
					// after that there's hopefully the string we are looking for
					// we will check if that string starts with <F> or <P> to verify that
					if (w != -1)
						continue;
					else {
						byte b=-1;
						while (b == -1 && mb.remaining()>8) {
							b = mb.get();
						}
						int old_position = mb.position()-1;
						mb.position(old_position);
						int i = mb.getInt();
						assert i != -1 || mb.remaining()<=8;
						if (mb.remaining() <= 8)
							break;
						assert i != -1;
						if (i>mb.remaining())
							continue; // not a string
						// one step back and try to read that string
						mb.position(old_position);
						String possibleTreeString = Utils.getString(mb);
						if (possibleTreeString.startsWith("<P>") ||
								possibleTreeString.startsWith("<F>")) {
							treeString = possibleTreeString;
						} else {
							mb.position(old_position+4);	
						}
					}
				}
			} else {
				ch = plorgcfg.getChannel();
				mb = ch.map(FileChannel.MapMode.READ_ONLY, 0L, ch.size());
				mb.order(ByteOrder.LITTLE_ENDIAN);
				mb.position(32);
				int bytesLeft = mb.getInt();
				mb.position(mb.position() + 16);
				String title_formatting = Utils.getString(mb);
				mb.position(mb.position() + 16);
				treeString = Utils.getString(mb);
				mb.position(mb.position() + 16);
				int unknown10 = mb.getInt(); // 456
			}
		}
		return treeString;

	}
}
