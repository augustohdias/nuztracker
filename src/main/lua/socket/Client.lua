buffer = console:createBuffer("Client")
buffer:setSize(1000, 1000)

client = socket.connect("127.0.0.1", 25322)

function getAddressFromPointer(offset)
    local address = 0
    for i = 3, 0, -1 do
        address = address + emu:read8(offset + i) * (16 ^ (2 * i))
    end
    return address
end

function readRange(address)
    return readRange(address, 1)
end

function readRange(address, size)
    local message = ""
    for i = 0, size do
        message = string.format(message .. "%x ", emu:read8(address + i))
    end
    return message
end

function sendStates()
    local nameAddress = getAddressFromPointer(0x03005d90)
    local nameMessage = "NAM|" .. readRange(nameAddress, 7)
    client:send(nameMessage .. '\n')

    local localizationAddress = getAddressFromPointer(0x03005d8c)
    local localizationMessage = "LOC|" .. readRange(localizationAddress + 4, 8)
    client:send(localizationMessage .. '\n')

    local caughtAddress = 0x02022e24
    local caughtOffset = 8
    local caughtMessage = "PXC|" .. readRange(caughtAddress + caughtOffset, 30)
    client:send(caughtMessage .. '\n')

    local seenOffset = 0x018 + 0x044
    local seenAddress = getAddressFromPointer(0x03005d90)
    local seenMessage = "PXS|" .. readRange(seenAddress + seenOffset, 0x034)
    client:send(seenMessage .. '\n')
end

function sendReset()
    local message = "RESET|00"
    client:send(message .. '\n')
end

function sendSave()
    local message = "SAV|00"
    client:send(message .. '\n')
end

cbidPoll = callbacks:add("keysRead", sendStates)

cbidPoll = callbacks:add("reset", sendReset)

cbidPoll = callbacks:add("savedataUpdated", sendSave)