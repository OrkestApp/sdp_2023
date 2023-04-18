from flask import Flask, request, render_template

app = Flask(__name__)

@app.route('/deezer')
def custom_url():
    arg = request.args.get('code')
    # Generate the custom URL based on the argument
    #custom_url = "https://orkest.page.link/deezer/{}".format(arg)
    custom_url = "https://orkest.page.link/?link=https://orkest/deezer/oauth?code={}&&apn=com.github.orkest&afl=https://orkest/deezer/oauth?param1=value1&param2=value2".format(arg)


    # Render the HTML template with the custom URL
    return render_template('home.html', custom_url=custom_url)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
