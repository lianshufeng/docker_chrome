# chrome



- Command Line
````shell
docker run --rm -v $(pwd):/tmp/chrome -e ENTRYPOINT="chrome --disable-dev-shm-usage --no-sandbox --headless --disable-gpu --window-size=1920,6422 --screenshot=/tmp/chrome/capture.png --process-per-site http://www.qq.com" lianshufeng/chrome
````

- api 
````shell
docker run --restart=always -d -p 8080:8080 --name chrome lianshufeng/chrome

eg:
http://127.0.0.1:8080/capture?url=http://www.baidu.com
````