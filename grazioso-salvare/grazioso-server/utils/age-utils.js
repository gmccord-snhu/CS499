function parseFlexibleDate(dateString) {
  if (!dateString || typeof dateString !== 'string' || dateString === 'N/A') {
    return null;
  }

  // First try native parsing for ISO-like values
  const nativeParsed = new Date(dateString);
  if (!isNaN(nativeParsed.getTime())) {
    return nativeParsed;
  }

  // Try M/D/YYYY H:MM or MM/DD/YYYY HH:MM
  let match = dateString.match(
    /^(\d{1,2})\/(\d{1,2})\/(\d{4})\s+(\d{1,2}):(\d{2})$/
  );

  if (match) {
    const month = parseInt(match[1], 10) - 1;
    const day = parseInt(match[2], 10);
    const year = parseInt(match[3], 10);
    const hour = parseInt(match[4], 10);
    const minute = parseInt(match[5], 10);

    const parsed = new Date(year, month, day, hour, minute);
    return isNaN(parsed.getTime()) ? null : parsed;
  }

  // Try MM/DD/YYYY HH:MM:SS AM/PM
  match = dateString.match(
    /^(\d{1,2})\/(\d{1,2})\/(\d{4})\s+(\d{1,2}):(\d{2}):(\d{2})\s+(AM|PM)$/i
  );

  if (match) {
    const month = parseInt(match[1], 10) - 1;
    const day = parseInt(match[2], 10);
    const year = parseInt(match[3], 10);
    let hour = parseInt(match[4], 10);
    const minute = parseInt(match[5], 10);
    const second = parseInt(match[6], 10);
    const ampm = match[7].toUpperCase();

    if (ampm === 'PM' && hour !== 12) hour += 12;
    if (ampm === 'AM' && hour === 12) hour = 0;

    const parsed = new Date(year, month, day, hour, minute, second);
    return isNaN(parsed.getTime()) ? null : parsed;
  }

  return null;
}

function calculateAgeInWeeks(dateOfBirth, outcomeDate) {
  const dob = parseFlexibleDate(dateOfBirth);
  const outcome = parseFlexibleDate(outcomeDate);

  if (!dob || !outcome) {
    return null;
  }

  const diffMs = outcome.getTime() - dob.getTime();

  if (diffMs < 0) {
    return null;
  }

  return Math.floor(diffMs / (1000 * 60 * 60 * 24 * 7));
}

module.exports = {
  parseFlexibleDate,
  calculateAgeInWeeks
};