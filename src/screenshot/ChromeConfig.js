module.exports = {
    'cmd': 'chrome --incognito --disable-dev-shm-usage --no-sandbox --headless --disable-gpu --window-size=${w},${h} --screenshot=${path} --process-per-site "${url}"'
}
