package com.yoyling;

import com.yoyling.listener.TextFieldHintListener;
import com.yoyling.tools.ClipboardUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JDownloader extends JFrame {

	public JPanel contentPane;
	public JTextField downloadUrlTextField;
	public static JTextField savedLocationTextField;
	public JTextField numberOfThreadsTextField;
	public JTextField refererTextField;
	public JLabel tipLabel;
	public JProgressBar progressBar;
	public JButton startDownloadButton;
	public JButton selectDirectoryButton;


	public static void main(String[] args) {
		JDownloader frame = new JDownloader();
		frame.addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				savedLocationTextField.requestFocusInWindow();
			}
		});
		frame.setVisible(true);
	}


	public JDownloader() {

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("多线程下载器");
		setBounds(100, 100, 600, 400);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		setLocationRelativeTo(null);

		downloadUrlTextField = new JTextField();
		downloadUrlTextField.setHorizontalAlignment(SwingConstants.CENTER);
		downloadUrlTextField.setToolTipText("");
		downloadUrlTextField.setBounds(49, 79, 396, 45);
		downloadUrlTextField.setFont(new Font("alias", Font.BOLD, 15));
		contentPane.add(downloadUrlTextField);
		downloadUrlTextField.setColumns(10);
		downloadUrlTextField.addFocusListener(new TextFieldHintListener(downloadUrlTextField,"键入下载链接"));
		downloadUrlTextField.setForeground(Color.BLACK);
		
		selectDirectoryButton = new JButton("...");
		selectDirectoryButton.setBounds(459, 156, 90, 45);
		contentPane.add(selectDirectoryButton);
		
		savedLocationTextField = new JTextField();
		savedLocationTextField.setHorizontalAlignment(SwingConstants.CENTER);
		savedLocationTextField.setColumns(10);
		savedLocationTextField.setBounds(49, 156, 396, 45);
		savedLocationTextField.setFont(new Font("alias", Font.BOLD, 15));
		savedLocationTextField.addFocusListener(new TextFieldHintListener(savedLocationTextField,"保存位置"));
		savedLocationTextField.setForeground(Color.BLACK);
		contentPane.add(savedLocationTextField);
		
		JLabel numberOfThreadLabel = new JLabel("线程数");
		numberOfThreadLabel.setBounds(480, 59, 50, 18);
		contentPane.add(numberOfThreadLabel);
		
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setBounds(0, 307, 594, 28);
		progressBar.setStringPainted(true);
		contentPane.add(progressBar);
		
		tipLabel = new JLabel("当前无下载任务！");
		tipLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tipLabel.setBounds(49, 334, 500, 18);
		contentPane.add(tipLabel);
		
		numberOfThreadsTextField = new JTextField();
		numberOfThreadsTextField.setFont(new Font("微软雅黑", Font.PLAIN, 30));
		numberOfThreadsTextField.setHorizontalAlignment(SwingConstants.CENTER);
		numberOfThreadsTextField.setText("8");
		numberOfThreadsTextField.setToolTipText("最好1-12线程内");
		numberOfThreadsTextField.setColumns(10);
		numberOfThreadsTextField.setBounds(459, 79, 90, 45);
		contentPane.add(numberOfThreadsTextField);
		
		File desktopDir = FileSystemView.getFileSystemView() .getHomeDirectory();
		String desktopPath = desktopDir.getAbsolutePath();
		savedLocationTextField.setText(desktopPath);
		
		JLabel label = new JLabel("多线程下载器");
		label.setForeground(Color.DARK_GRAY);
		label.setFont(new Font("微软雅黑", Font.BOLD, 35));
		label.setBounds(184, 13, 231, 53);
		contentPane.add(label);
		
		startDownloadButton = new JButton("下载");
		startDownloadButton.setBounds(459, 231, 90, 45);
		contentPane.add(startDownloadButton);
		
		refererTextField = new JTextField();
		refererTextField.setToolTipText("");
		refererTextField.setHorizontalAlignment(SwingConstants.CENTER);
		refererTextField.setForeground(Color.BLACK);
		refererTextField.setFont(new Font("Dialog", Font.BOLD, 15));
		refererTextField.setColumns(10);
		refererTextField.setBounds(49, 231, 396, 45);
		refererTextField.setFont(new Font("alias", Font.BOLD, 15));
		refererTextField.addFocusListener(new TextFieldHintListener(refererTextField,"Referer（可选）"));
		contentPane.add(refererTextField);
		
		JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(desktopDir);
		
		selectDirectoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = fileChooser.showSaveDialog(null);
				//保存为0，取消为1
				if(a == 0) {
					String t = fileChooser.getSelectedFile().getPath();
					savedLocationTextField.setText(t);
					savedLocationTextField.setForeground(Color.BLACK);
				}
				
			}
		});

		startDownloadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ("下载".equals(startDownloadButton.getText())) {
					new Downloader(downloadUrlTextField.getText().trim(), Integer.parseInt(numberOfThreadsTextField.getText().trim())).start();
				} else {
					System.exit(0);
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
				int t = JOptionPane.showConfirmDialog(contentPane, "确认退出?", "提示", JOptionPane.YES_NO_OPTION);
				if (t == 0) {
					System.exit(0);
				}
			}
		});
		
		downloadUrlTextField.setText(ClipboardUtil.getClipboardText());

	}


	public class Downloader extends Thread{

		private static final int DEFAULT_THREAD_COUNT = 8;  // 默认线程数量
		private AtomicBoolean canceled; // 取消状态，如果有一个子线程出现异常，则取消整个下载任务
		private DownloadFile file; // 下载的文件对象
		private String storageLocation;
		private final int threadCount; // 线程数量
		private long fileSize; // 文件大小
		private final String url;
		private long beginTime; // 开始时间

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
			System.out.println("* 开始下载：" + url);
			startDownloadButton.setEnabled(false);
			if (-1 == (this.fileSize = getFileSize()))
				return;

			System.out.printf("* 文件大小：%.2fMB\n", fileSize / 1024.0 / 1024);
			selectDirectoryButton.setText(String.format("%.2fMB",fileSize / 1024.0 / 1024));
			selectDirectoryButton.setEnabled(false);
			startDownloadButton.setText("取消");
			this.beginTime = System.currentTimeMillis();
			try {
				this.file = new DownloadFile(savedLocationTextField.getText().trim() + "/" + storageLocation, fileSize);
				// 分配线程下载
				dispatcher();
				// 循环打印进度
				printDownloadProgress();

			} catch (IOException e) {
				System.err.println("x 创建文件失败[" + e.getMessage() + "]");
				startDownloadButton.setEnabled(true);
				startDownloadButton.setText("下载");
				selectDirectoryButton.setEnabled(true);
				tipLabel.setText("当前无下载任务！");
				JOptionPane.showMessageDialog(contentPane, "下载失败，任务已取消！","提示",JOptionPane.ERROR_MESSAGE);
				selectDirectoryButton.setText("...");
			}
		}

		/**
		 * 分配器，决定每个线程下载哪个区间的数据
		 */
		private void dispatcher() {
			long blockSize = fileSize / threadCount; // 每个线程要下载的数据量
			long lowerBound = 0, upperBound = 0;
			long[][] bounds = null;
			int threadID = 0;
			for (int i = 0; i < threadCount; i++) {
				threadID = i;
				lowerBound = i * blockSize;
				// fileSize-1 !!!!! fu.ck，找了一下午的错
				upperBound = (i == threadCount - 1) ? fileSize-1 : lowerBound + blockSize;

				if ("".equals(refererTextField.getText().trim())) {
					new DownloadTask(url, lowerBound, upperBound, file, canceled, threadID).start();
				} else {
					new DownloadTask(url, lowerBound, upperBound, file, canceled, threadID, refererTextField.getText().trim()).start();
				}

			}
		}

		/**
		 * 循环打印进度，直到下载完毕，或任务被取消
		 */
		private void printDownloadProgress() {
			long downloadedSize = file.getWroteSize();
			int i = 0;
			long lastSize = 0; // 三秒前的下载量

			while (!canceled.get() && downloadedSize < fileSize) {
				progressBar.setValue((int)(downloadedSize / (double)fileSize * 100));
				progressBar.setString(String.format("%.1f %%", downloadedSize / (double)fileSize * 100));
				tipLabel.setText(String.format("已下载：%.2f MB   当前速度：%.1f MB/s",
						downloadedSize / 1024.0 / 1024,
						(downloadedSize - lastSize) / 1024.0 / 1024 * 8));
//				System.out.printf("下载进度：%.2f%%, 已下载：%.2fMB，当前速度：%.2fMB/s\n",
//						downloadedSize / (double)fileSize * 100 ,
//						downloadedSize / 1024.0 / 1024,
//						(downloadedSize - lastSize) / 1024.0 / 1024 * 4);
				lastSize = downloadedSize;
				try {
					Thread.sleep(175);
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
				System.err.println("下载失败，任务已取消！");
				startDownloadButton.setEnabled(true);
				startDownloadButton.setText("下载");
				selectDirectoryButton.setEnabled(true);
				tipLabel.setText("当前无下载任务！");
				JOptionPane.showMessageDialog(contentPane, "下载失败，任务已取消！","提示",JOptionPane.ERROR_MESSAGE);
				selectDirectoryButton.setText("...");
			} else {
//				System.out.println("* 下载成功，本次用时"+ (System.currentTimeMillis() - beginTime) / 1000 +"秒");
				progressBar.setValue(100);
				tipLabel.setText(String.format("下载完成，文件大小：%.2f MB，本次用时：%d 秒，平均速度：%.1f MB/S",fileSize/1024.0/1024,(System.currentTimeMillis() - beginTime) / 1000, fileSize/1024.0/1024/((System.currentTimeMillis() - beginTime) / 1000)));
				selectDirectoryButton.setText("...");
				selectDirectoryButton.setEnabled(true);
				startDownloadButton.setText("下载");
				downloadUrlTextField.setText("");
				downloadUrlTextField.addFocusListener(new TextFieldHintListener(downloadUrlTextField,"键入下载链接"));
				savedLocationTextField.requestFocus();
				progressBar.setValue(0);
				progressBar.setString("0%");
				refererTextField.setText("");
				refererTextField.addFocusListener(new TextFieldHintListener(refererTextField,"Referer（可选）"));
				JOptionPane.showMessageDialog(contentPane, "下载完成，本次用时" + (System.currentTimeMillis() - beginTime) / 1000 +"秒","提示",JOptionPane.QUESTION_MESSAGE);
				tipLabel.setText("当前无下载任务！");

			}
		}

		/**
		 * @return 要下载的文件的尺寸
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
				conn.connect();
				Map headers = conn.getHeaderFields();

				String storageLocationName = "";
				for(Object key : headers.keySet()){
					String val = conn.getHeaderField((String) key);
//					System.out.println(key +"   "+ val);
					if ("Content-Disposition".equals(key) || "content-Disposition".equals(key)) {
						storageLocationName = val;
						System.out.println(key+"  "+val);
						break;
					}
				}

				//流下载
				if (!"".equals(storageLocationName)) {
					//流下载
					Pattern pattern = Pattern.compile("filename=\"*(.*)\"?");
					Matcher matcher = pattern.matcher(storageLocationName);
					if (matcher.find()) {
						System.out.println(matcher.group(1));
						storageLocationName = matcher.group(1);
					}
					if (storageLocationName.endsWith("\"")) {
						storageLocationName = storageLocationName.substring(0,storageLocationName.length()-1);
					}
					storageLocation = storageLocationName;
				}
				startDownloadButton.setEnabled(true);
				System.out.println("* 连接服务器成功");
			} catch (MalformedURLException e) {
				JOptionPane.showMessageDialog(contentPane, "URL错误!","提示",JOptionPane.ERROR_MESSAGE);
				startDownloadButton.setEnabled(true);
				throw new RuntimeException("URL错误");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(contentPane, "连接服务器失败!","提示",JOptionPane.ERROR_MESSAGE);
				System.err.println("x 连接服务器失败["+ e.getMessage() +"]");
				return -1;
			}
			return conn.getContentLengthLong();
		}
	}
}
