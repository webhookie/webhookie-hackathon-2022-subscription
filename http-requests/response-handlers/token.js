client.test("Request executed successfully", function () {
    client.assert(response.status === 200, "Response status is not 200");
    if(response.body.hasOwnProperty("id_token")) {
        client.global.set("token", response.body.id_token);
    } else if(response.body.hasOwnProperty("access_token")) {
        client.global.set("token", response.body.access_token);
    }
    client.global.set("refresh_token", response.body.refresh_token);
});
