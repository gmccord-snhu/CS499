const mongoose = require('mongoose');

const outcomeSchema = new mongoose.Schema({
    "ID": Number,
    "Outcome Date": String,
    "Animal ID": Number,
    "Name": String,
    "Outcome Status": String,
    "Euthanasia Reason": String,
    "Type": String,
    "Sex": String,
    "Spayed/Neutered": String,
    "Primary Breed": String,
    "Primary Color": String,
    "Secondary Color": String,
    "Date of Birth": String,
    "Intake Date": String,
    "Days in Shelter": Number,
    "Timestamp": String,
    "calculatedAgeWeeks": Number
}, { 
    collection: 'outcomes', 
    strict: false 
});

module.exports = mongoose.model('Outcome', outcomeSchema);