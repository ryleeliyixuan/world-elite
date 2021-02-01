import { Message } from 'element-ui'

const toaster = {
    success(message){
        Message({
            message: message || 'Error',
            type: 'success',
            duration: 5 * 1000
          })
    },
    error(message){
        Message({
            message: message || 'Error',
            type: 'error',
            duration: 5 * 1000
          })
    },
    close(){
        Message.closeAll();
    }
}

export default toaster;
