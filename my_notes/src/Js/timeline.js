let  sendEvent = function (event) {
    this.$axios.post("/web/timeline",event).then((res) => {
        return res.data.data.data;
    })
}

export default sendEvent