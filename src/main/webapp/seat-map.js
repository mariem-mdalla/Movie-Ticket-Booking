document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("booking-form");
    if (!form) {
        return;
    }

    const seatInputs = Array.from(form.querySelectorAll('input[name="seatIds"]'));
    const selectedSeatsElement = document.getElementById("selected-seats");
    const totalElement = document.getElementById("booking-total");
    const confirmButton = document.getElementById("confirm-booking");
    const messageElement = document.getElementById("seat-update-message");
    const unitPrice = Number(form.dataset.price);

    function updateSummary() {
        const selectedInputs = seatInputs.filter((input) => input.checked && !input.disabled);
        selectedSeatsElement.textContent = selectedInputs.length
            ? selectedInputs.map((input) => input.dataset.label).join(", ")
            : "None";
        totalElement.textContent = (unitPrice * selectedInputs.length).toFixed(2);
        confirmButton.disabled = selectedInputs.length === 0;

        seatInputs.forEach((input) => {
            const label = input.closest("label");
            if (input.disabled) {
                label.classList.add("taken");
                label.classList.remove("free", "selected");
            } else if (input.checked) {
                label.classList.add("selected");
                label.classList.remove("free", "taken");
            } else {
                label.classList.add("free");
                label.classList.remove("selected", "taken");
            }
        });
    }

    seatInputs.forEach((input) => input.addEventListener("change", updateSummary));
    updateSummary();

    async function refreshTakenSeats() {
        try {
            const response = await fetch(form.dataset.statusUrl, { cache: "no-store" });
            if (!response.ok) {
                return;
            }
            const status = await response.json();
            const takenIds = new Set(status.takenSeatIds.map(String));
            let newlyTakenLabel = null;

            seatInputs.forEach((input) => {
                if (takenIds.has(input.value) && !input.disabled) {
                    if (input.checked) {
                        newlyTakenLabel = input.dataset.label;
                    }
                    input.checked = false;
                    input.disabled = true;
                }
            });

            if (newlyTakenLabel) {
                messageElement.textContent = `${newlyTakenLabel} was just taken by someone else`;
            }
            updateSummary();
        } catch (error) {
            // A temporary polling failure should not prevent the form from working.
        }
    }

    window.setInterval(refreshTakenSeats, 5000);
});
