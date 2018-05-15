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
 * �ɻ���Ϸ��������
 * @author oaoen
 *
 */
public class MyGameFrame extends JFrame{//�̳�JFrame��
	
	
	//ʹ�ù����࣬����ͼƬ
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
	int period;//��Ϸʱ��
	
	
	@Override
	public void paint(Graphics g) {//���ƴ��ڣ��Զ������á�g-->����
		Color c=g.getColor();
		super.paint(g);
		g.drawImage(bg, 0, 0, null);


		plane.drawSelf(g);//���ɻ�
		//plane2.drawSelf(g);
		shell.draw(g);
		//���������ڵ�
		for(int i=0;i<shells.length;i++) {
			shells[i].draw(g);
			
			boolean peng = shells[i].getRect().intersects(plane.getRect());
			
			if(peng) {
				//System.out.println("��ײ��!!!!!");
				plane.live=false;
				if(bao==null) {
				bao = new Explode(plane.x,plane.y);
				
				endTime = new Date();
				period = (int)((endTime.getTime()-startTime.getTime())/1000);
				}
				
				
				
				bao.draw(g);
			}
			//��ʱ
			if(!plane.live) {
			Font f =new Font("����",Font.BOLD,50);
			g.getFont();
			g.setColor(Color.red);
				g.drawString("ʱ�䣺"+period+"��",(int)plane.x,(int)plane.y);
			}
		}
		g.setColor(c);
		
	}
	
	class PaintThread extends Thread{
		
		@Override
		public void run() {
			while(true) {
				
				repaint();//�ػ�
				
				try {
					Thread.sleep(40);//1s=1000ms
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//���̼���
	class KeyMonitor extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			plane.addDirection(e);
			//super.keyPressed(e);
		//	System.out.println("����"+e.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent e) {
			plane.minusDirection(e);
			// TODO Auto-generated method stub
		//	super.keyReleased(e);
		//	System.out.println("����"+e.getKeyCode());
		}
		
		
	}
	
	
	/**
	 * ��ʼ������
	 */
		public void launchFrame() {
			this.setTitle("OAOEN_����Ա��Ʒ");//���ڱ���
			this.setVisible(true);//�Ƿ���ʾ
			this.setSize(Constant.GAME_HEIGHT,Constant.GAME_WIDTH);//���ڴ�С
			this.setLocation(300, 300);//����λ��
			
			this.addWindowListener(new WindowAdapter() {
			/**
			 * �رմ��ڲ���ֹͣ���̵�����/�˳�����
			 */
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}

				
			});
			
			new PaintThread().start();//���������ػ��߳�
			addKeyListener(new KeyMonitor());
		
		
		//��ʼ��50�ڵ�
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
