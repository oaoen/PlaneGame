package cn.sxt.game;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class GameUtil {
	//工具类私有化，无需new

		private GameUtil() {
}
		/**
		 * 指定图片路径
		 * @param path
		 * @return
		 */
		public static Image getImage(String path) {
			BufferedImage bi = null;
			try {
				URL u =GameUtil.class.getClassLoader().getResource(path);
				bi = ImageIO.read(u);
				}catch (IOException e) {
					e.printStackTrace();
				}
			return bi;
			}
		}