# chrome

````shell

docker run -v /opt/chrome:/opt/chrome lianshufeng/chrome chrome --no-sandbox --headless --disable-gpu --window-size=1920,1080 --screenshot=/opt/chrome/render.png http://www.baidu.com

````

- 内存溢出
````shell
docker run -v /opt/chrome:/opt/chrome lianshufeng/chrome chrome --disable-dev-shm-usage --no-sandbox --headless --disable-gpu --window-size=1920,6422 --screenshot=/opt/chrome/render.png --process-per-site http://www.qq.com

````

