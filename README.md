# MeteoDataProcessServer

OAuth2授权服务+自定义客户端+数据分析服务+数据获取程序——信创技术下气象数据采集分析系统

## 开发环境

OpenJDK 17

同时，该项目调用了Python脚本，您应该拥有Python解释器

Python 3.10

并且安装如下工具：

pip install pandas numpy sqlalchemy pymysql

## 运行项目

应按如下顺序运行

[OAuth2Custom-Service](https://github.com/organwalk/MeteoDataProcessServer/tree/master/OAuth2Custom-Service)

[OAuth2CustomClient-Service](https://github.com/organwalk/MeteoDataProcessServer/tree/master/OAuth2CustomClient-Service)

[Meteo-Process-Resource](https://github.com/organwalk/MeteoDataProcessServer/tree/master/Meteo-Process-Resource)

### 获取授权码

**GET请求**

```
http://localhost:9194/oauth2/authorize?response_type=code&client_id=meteo_client_one&scope=openid&redirect_uri=http://localhost:9294/authorized&code_challenge=QYPAZ5NU8yvtlQ9erXrUYR-T5AGCjCF47vN-KsaI2A8&code_challenge_method=S256
```

**测试用户**

| Username | Password |
| -------- | -------- |
| root     | 123456   |

**示例响应**

```json
{
    "success":1,
    "code":"04tPAQC9n1XdnWpkRnUuo0JeS6_ALIwQcqFLD32ydNB-hu-Gyc8LtmLMPOQpzFDnT4s2w2MGRAGricHNvHikLVBRxF8QldegkfXCAUTq30Uqnj0Lok8eDOvb07C1DNZb"
}
```

### 获取Jwt Token

**POST请求**

```
http://localhost:9194/oauth2/token?client_id=client&redirect_uri=http://localhost:9294/authorized&grant_type=authorization_code&code=7zAqUXUkUg_lKvbr8R771niLVbHh2TQ2v4YmlrOfHSxmotlu6_0SmUfSQpeiHVJiWcC-5KZC56NSyvNOTLPbraMgzcNfQoGa7MpUMh5xZfLLSoQ6ght0YZWvflRbkJRV&code_verifier=qPsH306-ZDDaOE8DFzVn05TkN3ZZoVmI_6x4LsVglQI
```

应该替换掉其中的code，修改为您所获得的授权码

**具有Basic Auth**

| Username         | Password         |
| ---------------- | ---------------- |
| meteo_client_one | meteo_secret_one |

一个示例响应：

```json
{
    "access_token": "eyJraWQiOiIzMmY0MjNjNi1kYmQwLTQ1MTktYmQ4NS03M2VkZmFiODNhOWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoibWV0ZW9fY2xpZW50X29uZSIsIm5iZiI6MTY4MzM1MDU3OSwic2NvcGUiOlsib3BlbmlkIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6OTE5NCIsImV4cCI6MTY4MzM5Mzc3OSwiaWF0IjoxNjgzMzUwNTc5LCJhdXRob3JpdGllcyI6WyJyZWFkIiwid3JpdGUiXX0.JDWGfOm-SLwpRQslBaId2TAgDULmT10yADmo6iFw5FzDYdckKXyoMIxwvgPReOPp6LQ3wwd3bDJkwDEY6PyXERqKOrdKf-HwHw_rCN7KjwtIn7ZFd-o3EiFbGMkuiPPJeIjDS-22mb18zicpU-izl2x7SaIa70WKxQ0f1ly2KuRGpUaYm_w7DvuCHZ6ie3Db96PPF2UpFFcws-aws_P9RouKFXSmcnsh1-3c6QdW4QBjUHGR8_yNHZ5n38MZlbP7NtQ-tekAYxsvGsM7rGBH8_Dw1w4Fk4kc6E77vt9cT5lXi-FmxvLpS3TRTDWiez0XoG9yT-2Rrzy0_j_cJBahFQ",
    "scope": "openid",
    "id_token": "eyJraWQiOiIzMmY0MjNjNi1kYmQwLTQ1MTktYmQ4NS03M2VkZmFiODNhOWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoibWV0ZW9fY2xpZW50X29uZSIsImF6cCI6Im1ldGVvX2NsaWVudF9vbmUiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjkxOTQiLCJleHAiOjE2ODMzNTIzNzksImlhdCI6MTY4MzM1MDU3OSwiYXV0aG9yaXRpZXMiOlsicmVhZCIsIndyaXRlIl19.CKGzXQOv-HBcLobekUzLfpjmw_FgCtihgnAmFcs8KSAksquSCs193-ViJESE1JtVV3Lx87YApKOcrA1vKUgl6TWK8H6R0HhM41Uzb-pFGhJZhdcfh6_3G2iQVZQ2GZy76a1gws8_yNd9aaE24qo5CGLrKne15AmRex_jClIQR6p-3uqG-_arcajb4NdYcBIeZhec8BXLpCucyJaaOzncOGEEtfjY_rY92BU16cIvudL5Pxz54JbOP48wSGLgCb0exyZiArNtPgr4JfIaKULcMpCEsogXCVeAYMa7nkTJ8TcO7VQm3G5P9oBN4I37PqcLHCwdn3NgRcBlHvC4irWDqw",
    "token_type": "Bearer",
    "expires_in": 43199
}
```

复制得到的access_token。

### **获取资源服务数据**

获取气象站编号

**GET请求**

```
http://localhost:9394/qx/stations
```

具有请求头，参数为access_token:

```
eyJraWQiOiIzMmY0MjNjNi1kYmQwLTQ1MTktYmQ4NS03M2VkZmFiODNhOWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwiYXVkIjoibWV0ZW9fY2xpZW50X29uZSIsIm5iZiI6MTY4MzM1MDU3OSwic2NvcGUiOlsib3BlbmlkIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6OTE5NCIsImV4cCI6MTY4MzM5Mzc3OSwiaWF0IjoxNjgzMzUwNTc5LCJhdXRob3JpdGllcyI6WyJyZWFkIiwid3JpdGUiXX0.JDWGfOm-SLwpRQslBaId2TAgDULmT10yADmo6iFw5FzDYdckKXyoMIxwvgPReOPp6LQ3wwd3bDJkwDEY6PyXERqKOrdKf-HwHw_rCN7KjwtIn7ZFd-o3EiFbGMkuiPPJeIjDS-22mb18zicpU-izl2x7SaIa70WKxQ0f1ly2KuRGpUaYm_w7DvuCHZ6ie3Db96PPF2UpFFcws-aws_P9RouKFXSmcnsh1-3c6QdW4QBjUHGR8_yNHZ5n38MZlbP7NtQ-tekAYxsvGsM7rGBH8_Dw1w4Fk4kc6E77vt9cT5lXi-FmxvLpS3TRTDWiez0XoG9yT-2Rrzy0_j_cJBahFQ
```

**示例响应**

```json
{
    "success": 1,
    "station": [
        {
            "station": "m2-403",
            "name": "一号气象站"
        },
        {
            "station": "m2-404",
            "name": "二号气象站"
        }
    ]
}
```

