package com.yoyling;

import com.yoyling.listener.TextFieldHintListener;
import com.yoyling.tools.ClipboardUtil;
import com.yoyling.tools.ConvertUtil;
import com.yoyling.tools.UrlUtil;
import com.yoyling.ui.CustomButton;
import com.yoyling.ui.ColorResource;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JDownloader extends JFrame {

	public static JPanel contentPane;
	public static JTextField downloadUrlTextField;
	public static JTextField savedLocationTextField;
	public JTextField numberOfThreadsTextField;
	public static JTextField refererTextField;
	public static JLabel tipLabel;
	public static JProgressBar progressBar;
	public static JButton startDownloadButton;
	public static JButton selectDirectoryButton;

	public static boolean cancelDownload = false;


	public static void main(String[] args) {
		JDownloader frame = new JDownloader();
		frame.addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				downloadUrlTextField.requestFocusInWindow();
				downloadUrlTextField.setCaretPosition(downloadUrlTextField.getText().length());
			}
		});
		frame.setVisible(true);
	}


	public JDownloader() {
//		setUndecorated(true);
		UIManager.put("TextField.caretForeground", new ColorUIResource(Color.WHITE));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("JDownloader");
		setBounds(100, 100, 600, 400);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(JDownloader.class.getResource("/icons/favicon.png")));
		contentPane = new JPanel();
		contentPane.setBackground(ColorResource.getDarkestBgColor());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		setLocationRelativeTo(null);

		try {
			UIManager.setLookAndFeel(new MetalLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}

		downloadUrlTextField = new JTextField();
		downloadUrlTextField.setHorizontalAlignment(SwingConstants.CENTER);
		downloadUrlTextField.setToolTipText("");
		downloadUrlTextField.setBounds(49, 79, 396, 45);
		downloadUrlTextField.setFont(new Font("alias", Font.BOLD, 15));
		contentPane.add(downloadUrlTextField);
		downloadUrlTextField.setColumns(10);
		downloadUrlTextField.addFocusListener(new TextFieldHintListener(downloadUrlTextField,"??????????????????"));
		downloadUrlTextField.setForeground(Color.WHITE);
		downloadUrlTextField.setBackground(new Color(50, 50, 50));
		downloadUrlTextField.setBorder(BorderFactory.createLineBorder(new Color(82, 82, 82), 1));

		selectDirectoryButton = new CustomButton("...");
		selectDirectoryButton.setBounds(459, 156, 90, 45);
		selectDirectoryButton.setForeground(Color.white);
		selectDirectoryButton.setBackground(ColorResource.getDarkBgColor());
		selectDirectoryButton.setBorderPainted(false);
		selectDirectoryButton.setFocusPainted(false);


		contentPane.add(selectDirectoryButton);
		
		savedLocationTextField = new JTextField();
		savedLocationTextField.setHorizontalAlignment(SwingConstants.CENTER);
		savedLocationTextField.setColumns(10);
		savedLocationTextField.setBounds(49, 156, 396, 45);
		savedLocationTextField.setFont(new Font("alias", Font.BOLD, 15));
		savedLocationTextField.addFocusListener(new TextFieldHintListener(savedLocationTextField,"????????????"));
		savedLocationTextField.setForeground(Color.WHITE);
		savedLocationTextField.setBackground(new Color(50, 50, 50));
		savedLocationTextField.setBorder(BorderFactory.createLineBorder(new Color(82, 82, 82), 1));
		contentPane.add(savedLocationTextField);


		
		JLabel numberOfThreadLabel = new JLabel("?????????");
		numberOfThreadLabel.setBounds(480, 59, 50, 18);
		numberOfThreadLabel.setForeground(new Color(130, 130, 130));
		contentPane.add(numberOfThreadLabel);
		
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setBounds(0, 307, 594, 20);
		progressBar.setStringPainted(true);
		progressBar.setBackground(new Color(38, 38, 38));
		progressBar.setBorderPainted(false);
		contentPane.add(progressBar);
		
		tipLabel = new JLabel("????????????????????????");
		tipLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tipLabel.setBounds(49, 334, 500, 18);
		tipLabel.setForeground(new Color(199, 199, 199));
		contentPane.add(tipLabel);
		
		numberOfThreadsTextField = new JTextField();
		numberOfThreadsTextField.setFont(new Font("????????????", Font.PLAIN, 30));
		numberOfThreadsTextField.setHorizontalAlignment(SwingConstants.CENTER);
		numberOfThreadsTextField.setText("8");
		numberOfThreadsTextField.setToolTipText("??????1-12?????????");
		numberOfThreadsTextField.setColumns(10);
		numberOfThreadsTextField.setBounds(459, 79, 90, 45);


		numberOfThreadsTextField.setForeground(Color.WHITE);
		numberOfThreadsTextField.setBackground(new Color(50, 50, 50));
		numberOfThreadsTextField.setBorder(BorderFactory.createLineBorder(new Color(82, 82, 82), 1));
		contentPane.add(numberOfThreadsTextField);
		
		File desktopDir = FileSystemView.getFileSystemView() .getHomeDirectory();
		String desktopPath = desktopDir.getAbsolutePath();
		savedLocationTextField.setText(desktopPath);
		
		JLabel label = new JLabel("??? ??? ??? ??? ??? ???");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("????????????", Font.BOLD, 35));
		label.setBounds(165, 13, 300, 53);
		contentPane.add(label);
		
		startDownloadButton = new CustomButton("??????");
		startDownloadButton.setBounds(459, 231, 90, 45);
		startDownloadButton.setForeground(Color.white);
		startDownloadButton.setBackground(ColorResource.getDarkBgColor());
		startDownloadButton.setBorderPainted(false);
		startDownloadButton.setFocusPainted(false);
		contentPane.add(startDownloadButton);
		
		refererTextField = new JTextField();
		refererTextField.setToolTipText("");
		refererTextField.setHorizontalAlignment(SwingConstants.CENTER);
		refererTextField.setForeground(Color.BLACK);
		refererTextField.setFont(new Font("Dialog", Font.BOLD, 15));
		refererTextField.setColumns(10);
		refererTextField.setBounds(49, 231, 396, 45);
		refererTextField.setFont(new Font("alias", Font.BOLD, 15));
		refererTextField.addFocusListener(new TextFieldHintListener(refererTextField,"Referer????????????"));
		refererTextField.setForeground(Color.GRAY);
		refererTextField.setBackground(new Color(50, 50, 50));
		refererTextField.setBorder(BorderFactory.createLineBorder(new Color(82, 82, 82), 1));
		contentPane.add(refererTextField);
		
		JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(desktopDir);
		
		selectDirectoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = fileChooser.showSaveDialog(null);
				//?????????0????????????1
				if(a == 0) {
					String t = fileChooser.getSelectedFile().getPath();
					savedLocationTextField.setText(t);
					savedLocationTextField.setForeground(Color.white);
				}
				
			}
		});

		startDownloadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ("??????".equals(startDownloadButton.getText()) && !"".equals(downloadUrlTextField.getText().trim())) {
					new Downloader(downloadUrlTextField.getText().trim(), Integer.parseInt(numberOfThreadsTextField.getText().trim())).start();
				} else {
					if ("??????".equals(startDownloadButton.getText())) {
						String s = savedLocationTextField.getText().trim() + File.separator + Downloader.storageLocation;
						cancelDownload = true;
						try {
							if (new File(s).exists()) {
								new File(s).delete();
							}
						} catch (Exception ex) {
							System.err.println("?????????????????????");
						}
						System.out.println(Downloader.storageLocation);

//						System.exit(0);
//						Files.delete(Paths.get(savedLocationTextField.getText().trim() + "/" + Downloader.storageLocation));
					} else {
						JOptionPane.showMessageDialog(contentPane, "?????????????????????????????????","??????",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		numberOfThreadsTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (!numberOfThreadsTextField.getText().trim().matches("[0-9]+")) {
					numberOfThreadsTextField.setText("8");
				} else {
					int t = Integer.parseInt(numberOfThreadsTextField.getText().trim());
					if (t < 1 || t > 12) {
						numberOfThreadsTextField.setText("8");
					}
				}
			}
		});

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				int t = JOptionPane.showConfirmDialog(contentPane, "?????????????", "??????", JOptionPane.YES_NO_OPTION);
				if (t == 0) {
					System.exit(0);
				}
			}
		});

		if (!"".equals(ClipboardUtil.getClipboardText())) {
			downloadUrlTextField.setText(ClipboardUtil.getClipboardText());
		} else {
			downloadUrlTextField.setForeground(Color.gray);
		}
	}


	public static class Downloader extends Thread{

		private static final int DEFAULT_THREAD_COUNT = 8;  // ??????????????????
		private AtomicBoolean canceled; // ?????????????????????????????????????????????????????????????????????????????????
		private DownloadFile file; // ?????????????????????
		private static String storageLocation;
		private final int threadCount; // ????????????
		private long fileSize; // ????????????
		private final String url;
		private long beginTime; // ????????????

		public Downloader(String url) {
			this(url, DEFAULT_THREAD_COUNT);
		}

		public Downloader(String url, int threadCount) {
			this.url = url;
			this.threadCount = threadCount;
			this.canceled = new AtomicBoolean(false);
			this.storageLocation = url.substring(url.lastIndexOf('/')+1);
		}

		@Override
		public void run() {
			System.out.println("* ???????????????" + url);
			savedLocationTextField.setEnabled(false);
			startDownloadButton.setEnabled(false);
			startDownloadButton.setText("?????????...");

			this.fileSize = getFileSize();

			if (fileSize == -1) {
				startDownloadButton.setEnabled(true);
				startDownloadButton.setText("??????");
				savedLocationTextField.setEnabled(true);
				JOptionPane.showMessageDialog(contentPane, "????????????????????????","??????",JOptionPane.ERROR_MESSAGE);
				System.err.println("x ????????????????????????");
				return;
			} else if (fileSize == 0) {
				System.err.println("?????????????????????????????????");
				startDownloadButton.setText("??????");
				startDownloadButton.setEnabled(true);
				savedLocationTextField.setEnabled(true);
				JOptionPane.showMessageDialog(contentPane, "?????????????????????????????????","??????",JOptionPane.ERROR_MESSAGE);
				return;
			}

			System.out.println("* ???????????????" + ConvertUtil.converFileSize(this.fileSize));
			selectDirectoryButton.setText(ConvertUtil.converFileSize(this.fileSize));
			selectDirectoryButton.setEnabled(false);
			startDownloadButton.setText("??????");
			this.beginTime = System.currentTimeMillis();
			try {
				File saveDir = new File(savedLocationTextField.getText().trim() + "/");
				if (!saveDir.exists()) {
					if (saveDir.mkdir()) {
						System.out.println("* ??????????????????????????????");
					}else {
						System.out.println("* ??????????????????????????????");
					}
				}
				this.file = new DownloadFile(savedLocationTextField.getText().trim() + "/" + this.storageLocation, this.fileSize);

				// ??????????????????
				dispatcher();
				// ??????????????????
				printDownloadProgress();

			} catch (Exception e) {
				System.err.println("x ??????????????????[" + e.getMessage() + "]");
				startDownloadButton.setEnabled(true);
				startDownloadButton.setText("??????");
				selectDirectoryButton.setEnabled(true);
				savedLocationTextField.setEnabled(true);
				tipLabel.setText("????????????????????????");
				JOptionPane.showMessageDialog(contentPane, "?????????????????????????????????","??????",JOptionPane.ERROR_MESSAGE);
				selectDirectoryButton.setText("...");
			}
		}

		/**
		 * ?????????????????????????????????????????????????????????
		 */
		private void dispatcher() {
			long blockSize = fileSize / threadCount; // ?????????????????????????????????
			long lowerBound = 0, upperBound = 0;
			long[][] bounds = null;
			int threadID = 0;
			for (int i = 0; i < threadCount; i++) {
				threadID = i;
				lowerBound = i * blockSize;
				// fileSize-1 !!!!! fu.ck????????????????????????
				upperBound = (i == threadCount - 1) ? fileSize-1 : lowerBound + blockSize;

				if ("".equals(refererTextField.getText().trim())) {
					new DownloadTask(url, lowerBound, upperBound, file, canceled, threadID).start();
				} else {
					new DownloadTask(url, lowerBound, upperBound, file, canceled, threadID, refererTextField.getText().trim()).start();
				}

			}
		}

		/**
		 * ????????????????????????????????????????????????????????????
		 */
		private void printDownloadProgress() {
			long downloadedSize = file.getWroteSize();
			int i = 0;
			long lastSize = 0; // ?????????????????????
			String s = "";
			while (!canceled.get() && downloadedSize < fileSize) {
				progressBar.setValue((int)(downloadedSize / (double)fileSize * 100));
				progressBar.setString(String.format("%.2f %%", downloadedSize / (double)fileSize * 100));
				s = "????????????" + ConvertUtil.converFileSize(downloadedSize);
				tipLabel.setText(s + String.format("   ???????????????%.1f M/s", ((downloadedSize - lastSize) / 1024.0 / 1024)));
				lastSize = downloadedSize;
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					System.out.println(e);
				}
				downloadedSize = file.getWroteSize();
			}
			file.close();
			if (canceled.get()) {
				try {
					Files.delete(Paths.get(savedLocationTextField.getText().trim() + "/" + storageLocation));
				} catch (IOException ignore) {
				}
				System.err.println("?????????????????????????????????");
				progressBar.setValue(0);
				progressBar.setString("0 %");
				cancelDownload = false;
				startDownloadButton.setEnabled(true);
				savedLocationTextField.setEnabled(true);
				startDownloadButton.setText("??????");
				selectDirectoryButton.setEnabled(true);
				tipLabel.setText("????????????????????????");
				JOptionPane.showMessageDialog(contentPane, "?????????????????????????????????","??????",JOptionPane.ERROR_MESSAGE);
				selectDirectoryButton.setText("...");
			} else {
				progressBar.setValue(100);
				String s1 = "?????????" + ConvertUtil.converFileSize(fileSize) + "       ";
				String s2 = "?????????" + ConvertUtil.millisToStringShort(System.currentTimeMillis() - beginTime) + "       ";
				tipLabel.setText(s1 + s2 + String.format("?????????%.1f M/s",fileSize/1024.0/1024/((System.currentTimeMillis() - beginTime) / 1000)));
				selectDirectoryButton.setText("...");
				selectDirectoryButton.setEnabled(true);
				savedLocationTextField.setEnabled(true);
				startDownloadButton.setText("??????");
				downloadUrlTextField.setText("");
				downloadUrlTextField.addFocusListener(new TextFieldHintListener(downloadUrlTextField,"??????????????????"));
				savedLocationTextField.requestFocus();
				progressBar.setValue(0);
				progressBar.setString("0 %");
				refererTextField.setText("");
				refererTextField.addFocusListener(new TextFieldHintListener(refererTextField,"Referer????????????"));
				JOptionPane.showMessageDialog(contentPane, "?????????????????????" + ConvertUtil.millisToStringShort(System.currentTimeMillis() - beginTime),"??????",JOptionPane.QUESTION_MESSAGE);
				tipLabel.setText("????????????????????????");

			}
		}

		/**
		 * @return ???????????????????????????
		 */
		private long getFileSize() {
			if (fileSize != 0) {
				return fileSize;
			}
			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection)new URL(url).openConnection();
				conn.setConnectTimeout(3000);
				conn.setRequestMethod("HEAD");
				conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
				conn.connect();

				System.out.println("* ?????????: " + conn.getResponseCode());
				Map headers = conn.getHeaderFields();

				String storageLocationName = "";
				for(Object key : headers.keySet()){
					String val = conn.getHeaderField((String) key);
//					System.out.println(key +"   "+ val);
					if ("Content-Disposition".equals(key) || "content-Disposition".equals(key)) {
						storageLocationName = val;
						break;
					}
				}

				//?????????
				if (!"".equals(storageLocationName)) {
					//?????????
					Pattern pattern = Pattern.compile("filename=\"*(.*)\"?");
					Matcher matcher = pattern.matcher(storageLocationName);
					if (matcher.find()) {
						storageLocationName = matcher.group(1);
					}

//					storageLocationName = storageLocationName.replaceAll("\"", "");
					if (storageLocationName.endsWith("\"")) {
						storageLocationName = storageLocationName.substring(0,storageLocationName.length()-1);
						storageLocationName = UrlUtil.getURLDecoderString(storageLocationName);
						System.out.println("* ?????????: " + storageLocationName);
					}
					storageLocation = storageLocationName;
				}
				startDownloadButton.setEnabled(true);
				System.out.println("* ?????????????????????");
			} catch (MalformedURLException e) {
				JOptionPane.showMessageDialog(contentPane, "URL??????!","??????",JOptionPane.ERROR_MESSAGE);
				startDownloadButton.setEnabled(true);
				savedLocationTextField.setEnabled(true);
				startDownloadButton.setText("??????");
				throw new RuntimeException("URL??????");
			} catch (IOException e) {
				return -1;
			}
			return conn.getContentLengthLong();
		}
	}
}
