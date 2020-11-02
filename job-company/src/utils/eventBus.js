export default {
    eventBus: [],

    addEvent: function (name, event) {
        let eventBusItem = this.eventBus.find(eventBusItem => {
            return eventBusItem.name === name;
        })
        if (eventBusItem) {
            eventBusItem.event = event;
        } else {
            this.eventBus.push({name, event})
        }
    },

    removeEvent: function (name, event) {
        let eventBusItemIndex = this.eventBus.findIndex(eventBusItem => {
            return eventBusItem.name === name;
        })
        if (eventBusItemIndex >= 0) {
            this.eventBus.splice(eventBusItemIndex, 1)
        }
        console.log(this.eventBus);
    },

    emitEvent: function (name, value) {
        let eventBusItem = this.eventBus.find(eventBusItem => {
            return eventBusItem.name === name;
        })
        if (eventBusItem) {
            eventBusItem.event(value);
        }
    },

    clearEvent() {
        this.eventBus = [];
    }
}
