# chrome



- shell
````shell
docker run -v /opt/chrome:/opt/chrome lianshufeng/chrome chrome --disable-dev-shm-usage --no-sandbox --headless --disable-gpu --window-size=1920,6422 --screenshot=/opt/chrome/render.png --process-per-site http://www.qq.com

````

- web
````shell
docker run --restart=always -d -p 80:80 --name chrome lianshufeng/chrome  npm start
````