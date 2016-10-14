package LogicRegression.multipleLinearRegressionAnalysis;

import java.math.BigDecimal;
import java.util.ArrayList;

public class RegressionLine {

	/* x的数量 * */
	private double sumX;

	/* y的数量 * */
	private double sumY;

	/* x的平方的值 * */
	private double sumXX;

	/* x乘以y的值 * */
	private double sumXY;

	/* y的平方的值 * */
	private double sumYY;

	/* sumDeltaY的平方的值 * */
	private double sumDeltaY2;

	/* 误差 * */
	private double sse;

	private double sst;

	private double E;

	private String[] xy;

	private ArrayList listX;

	private ArrayList listY;

	private int XMin, XMax, YMin, YMax;

	/* 线系数a0 * */
	private float a0;

	/* 线系数a1 * */
	private float a1;

	/* 数据点数 * */
	private int pn;

	/* 记录系数是否有效 * */
	private boolean coefsValid;

	// 构造函数
	public RegressionLine() {
		XMax = 0;
		YMax = 0;
		pn = 0;
		xy = new String[2];
		listX = new ArrayList();
		listY = new ArrayList();
	}

	/*
	 * 返回当前数据点的数目
	 * 
	 * @return 当前数据点的数目
	 **/
	public int getDataPointCount() {
		return pn;
	}

	/*
	 * 返回线系数a0
	 ***/
	public float getA0() {
		validateCoefficients();
		return a0;
	}

	/*
	 * 返回线系数a1
	 ***/
	public float getA1() {
		validateCoefficients();
		return a1;
	}

	public double getSumX() {
		return sumX;
	}

	public double getSumY() {
		return sumY;
	}

	public double getSumXX() {
		return sumXX;
	}

	public double getSumXY() {
		return sumXY;
	}

	public double getSumYY() {
		return sumYY;
	}

	public int getXMin() {
		return XMin;
	}

	public int getXMax() {
		return XMax;
	}

	public int getYMin() {
		return YMin;
	}

	public int getYMax() {
		return YMax;
	}

	/*
	 * 计算方程系数 y=ax+b 中的a
	 ***/
	private void validateCoefficients() {

		if (coefsValid) {
			return;
		}
		if (pn >= 2) {
			float xBar = (float) (sumX / pn);
			float yBar = (float) (sumY / pn);

			a1 = (float) ((pn * sumXY - sumX * sumY) / (pn * sumXX - sumX
					* sumX));
			a0 = (float) (yBar - a1 * xBar);
		} else {
			a0 = a1 = Float.NaN;
		}
		coefsValid = true;
	}

	/*
	 * 添加一个新的数据点：改变总量
	 *********/
	public void addDataPoint(DataPoint dataPoint) {
		sumX += dataPoint.x;
		sumY += dataPoint.y;
		sumXX += dataPoint.x * dataPoint.x;
		sumXY += dataPoint.x * dataPoint.y;
		sumYY += dataPoint.y * dataPoint.y;

		if (dataPoint.x >= XMax) {
			XMax = (int) dataPoint.x;
		}
		if (dataPoint.y >= YMax) {
			YMax = (int) dataPoint.y;
		}

		// 把每个点的具体坐标存入ArrayList中，备用

		xy[0] = (int) dataPoint.x + "";
		xy[1] = (int) dataPoint.y + "";
		if (dataPoint.x != 0 && dataPoint.y != 0) {
			System.out.print(xy[0] + ",");
			System.out.println(xy[1]);

			try {
				listX.add(pn, xy[0]);
				listY.add(pn, xy[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		++pn;
		coefsValid = false;
	}

	/*
	 * 返回x的回归线函数的值
	 **/
	public float at(int x) {
		if (pn < 2)
			return Float.NaN;

		validateCoefficients();
		return a0 + a1 * x;
	}

	public void reset() {
		pn = 0;
		sumX = sumY = sumXX = sumXY = 0;
		coefsValid = false;
	}

	/**
	 * 返回误差
	 */
	public double getR() {
		// 遍历这个list并计算分母
		for (int i = 0; i < pn - 1; i++) {
			float Yi = (float) Integer.parseInt(listY.get(i).toString());
			float Y = at(Integer.parseInt(listX.get(i).toString()));
			float deltaY = Yi - Y;
			float deltaY2 = deltaY * deltaY;

			sumDeltaY2 += deltaY2;

		}

		sst = sumYY - (sumY * sumY) / pn;
		E = 1 - sumDeltaY2 / sst;

		return round(E, 4);
	}

	// 用于实现精确的四舍五入
	public double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"这个比例必须是一个正整数或零");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();//向"最接近的"数字舍入，如果与两个相邻数字的距离相等，则为向上舍入的舍入模式。

	}

	public float round(float v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"这个比例必须是一个正整数或零");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).floatValue();

	}

}
