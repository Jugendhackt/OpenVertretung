require 'net/http'
require 'uri'
require 'json'

uri = URI.parse("http://localhost:3000")

header = {'Content-Type': 'text/json'}
# get json from form


# Create the HTTP objects
http = Net::HTTP.new(uri.host, uri.port)
request = Net::HTTP::Post.new(uri.request_uri, header)
request.body = zeile.to_json

# Send the request
response = http.request(request)