
const toaster = {
    success(message){
        getVm().$bvToast.toast(message, {
            title: "提示",
            toaster: 'b-toaster-top-center',
            variant: "success"
          });
    },
    error(message){
        getVm().$bvToast.toast(message, {
            title: "提示",
            toaster: 'b-toaster-top-center',
            variant: "danger"
          });
    }
}

const getVm = () => document._vm_instance

export default toaster;