
import flask
from flask import  render_template, jsonify, request
import time
import os
import base64

app=flask.Flask(__name__)
upload_folder="C://Users/Fenil/Desktop/uploadedVideos"

app.config ["upload_folder"]=upload_folder
basedir=os.path.abspath (os.path.dirname ("C://Users/Fenil/Desktop/uploadedVideos"))
allowed_extensions=set (["txt", "png", "jpg", "jpeg", "xls", "jpg", "png", "xlsx", "gif", "gif", "mp4"])


def allowed_file (filename):
 return "." in filename and filename.rsplit (".", 1) [1] in allowed_extensions
#upload files

@ app.route ("/api/upload", methods=["POST"], strict_slashes=False)
def api_upload ():
 file_dir=os.path.join (basedir, app.config ["upload_folder"])
 
 if not os.path.exists (file_dir):
  os.makedirs (file_dir)

 
 f=request.files ["myfile"] 
 
 
 if f and allowed_file (f.filename):
  fname=f.filename
  print (fname," Uploaded to server")
  ext=fname.rsplit (".", 1) [1]
  unix_time=int (time.time ())
  new_filename=str (unix_time) + "." + ext 
  f.save (os.path.join (file_dir, fname)) 

  return ("Uploaded !! File name : \n"+fname)
 else:
  return jsonify ({"errno":1001, "errmsg":"Upload failed"})

@app.route('/', methods=['GET'])
def handle_get():
    return "GET msg mac m1"

app.run (host="0.0.0.0", port=5000,debug=True)
