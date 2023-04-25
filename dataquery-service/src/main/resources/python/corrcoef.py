import pandas as pd
import numpy as np
from sqlalchemy import create_engine
import sys

# 连接 MySQL 数据库
db = create_engine('mysql+pymysql://root:123456@localhost:3306/qxdatabase')


def get_correlation_matrix(station, start_date, end_date, correlation):
    # 构造表名
    table_name = station + '_weather_' + start_date[:4]

    # 读取数据
    query = "SELECT date, temperature, humidity, speed, direction, rain, sunlight, pm25, pm10 FROM %s WHERE date >= '%s' AND date < '%s'" % (table_name, start_date, end_date)
    df = pd.read_sql_query(query, con=db)

    # 删除缺失值
    df = df.dropna()

    # 选择需要计算相关性的列
    data = df.iloc[:, correlation].values

    # 计算相关性矩阵
    corr_matrix = np.corrcoef(data, rowvar=False)

    return corr_matrix.tolist()


# 解析命令行参数
station = sys.argv[1]
start_date = sys.argv[2]
end_date = sys.argv[3]
correlation = list(map(int, sys.argv[4].split(",")))
# 调用函数
result = get_correlation_matrix(station, start_date, end_date, correlation)
# 输出结果
print(result)