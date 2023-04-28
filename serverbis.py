from flask import Flask, request, render_template
import requests

app = Flask(__name__)

@app.route('/deezer')
def custom_url():
    arg = request.args.get('code')
    CLIENT_ID = "592224"
    CLIENT_SECRET = "7951a1e4171f70af65cae5c55fdd0e51"

    x = requests.post("https://connect.deezer.com/oauth/access_token.php?app_id=592224&secret=7951a1e4171f70af65cae5c55fdd0e51&code={}".format(arg))
    access_token = x.text.split("&")[0].split("=")[1]
    # Generate the custom URL based on the argument
    #custom_url = "https://orkest.page.link/deezer/{}".format(arg)
    custom_url = "https://orkest.page.link/?link=https://orkest/deezer/oauth?code={}&&apn=com.github.orkest&afl=https://orkest/deezer/oauth?code={}".format(access_token,access_token)


    # Render the HTML template with the custom URL
    return render_template('home.html', custom_url=custom_url)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
