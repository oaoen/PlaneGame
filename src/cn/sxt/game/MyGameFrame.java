package cn.sxt.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JFrame;

/**
 * 飞机游戏的主窗口
 * @author oaoen
 *
 */
public class MyGameFrame extends JFrame{//继承JFrame类
	
	
	//使用工具类，引用图片
	Image bg = GameUtil.getImage("images/bg.jpg");
	Image planeImg = GameUtil.getImage("images/plane.png");
	
	//int planeX = 250,planeY=250;
	Plane plane = new Plane(planeImg,250,250);
	//Plane plane2 = new Plane(planeImg,350,250);
	Shell shell = new Shell();
	Shell[] shells = new Shell[50];
	
	Explode bao;
	Date startTime = new Date();
	Date endTime;
	int period;//游戏时间
	
	
	@Override
	public void paint(Graphics g) {//绘制窗口，自动被调用。g-->画笔
		Color c=g.getColor();
		super.paint(g);
		g.drawImage(bg, 0, 0, null);


		plane.drawSelf(g);//画飞机
		//plane2.drawSelf(g);
		shell.draw(g);
		//画出所有炮弹
		for(int i=0;i<shells.length;i++) {
			shells[i].draw(g);
			
			boolean peng = shells[i].getRect().intersects(plane.getRect());
			
			if(peng) {
				//System.out.println("相撞了!!!!!");
				plane.live=false;
				if(bao==null) {
				bao = new Explode(plane.x,plane.y);
				
				endTime = new Date();
				period = (int)((endTime.getTime()-startTime.getTime())/1000);
				}
				
				
				
				bao.draw(g);
			}
			//计时
			if(!plane.live) {
			Font f =new Font("宋体",Font.BOLD,50);
			g.getFont();
			g.setColor(Color.red);
				g.drawString("时间："+period+"秒",(int)plane.x,(int)plane.y);
			}
		}
		g.setColor(c);
		
	}
	
	class PaintThread extends Thread{
		
		@Override
		public void run() {
			while(true) {
				
				repaint();//重画
				
				try {
					Thread.sleep(40);//1s=1000ms
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//键盘监听
	class KeyMonitor extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			plane.addDirection(e);
			//super.keyPressed(e);
		//	System.out.println("按下"+e.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent e) {
			plane.minusDirection(e);
			// TODO Auto-generated method stub
		//	super.keyReleased(e);
		//	System.out.println("按下"+e.getKeyCode());
		}
		
		
	}
	
	
	/**
	 * 初始化窗口
	 */
		public void launchFrame() {
			this.setTitle("OAOEN_程序员作品");//窗口标题
			this.setVisible(true);//是否显示
			this.setSize(Constant.GAME_HEIGHT,Constant.GAME_WIDTH);//窗口大小
			this.setLocation(300, 300);//窗口位置
			
			this.addWindowListener(new WindowAdapter() {
			/**
			 * 关闭窗口并听停止工程的运行/退出工程
			 */
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}

				
			});
			
			new PaintThread().start();//启动窗口重画线程
			addKeyListener(new KeyMonitor());
		
		
		//初始化50炮弹
		for(int i=0;i<shells.length;i++) {
			shells[i] = new Shell();
		}
			
		}	
		public static void main(String[] args) {
			MyGameFrame f = new MyGameFrame();
			f.launchFrame();
		}
		
		private Image offScreenImage = null;
		
		public void update(Graphics g) {
			if(offScreenImage == null)
				offScreenImage = this.createImage(Constant.GAME_HEIGHT,Constant.GAME_WIDTH);
			
			Graphics gOff = offScreenImage.getGraphics();
			paint(gOff);
			g.drawImage(offScreenImage, 0, 0, null);
				
		}
}
