//package org.Invoker.remoting.codec;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//import org.Invoker.remoting.exchanger.Channel;
//import org.Invoker.remoting.exchanger.Request;
//import org.Invoker.remoting.exchanger.Response;
//import org.Invoker.rpc.invoker.DecodeableRpcInvocation;
//import org.Invoker.rpc.invoker.Invocation;
//import org.Invoker.rpc.result.DecodeableRpcResult;
//
//public class DefaultCodec extends ExchangerCodec{
//
//	@Override
//	public Object decodeBody(Channel channel, InputStream is, byte[] header) throws IOException {
//        byte flag = header[2]; 
//        byte proto = (byte) (flag & SERIALIZATION_MASK);
//        Serialization s = CodecSupport.getSerialization(proto);
//        long id = Bytes.bytes2long(header, 4);
//        if ((flag & FLAG_REQUEST) == 0) {
//            Response res = new Response(id);
//            if ((flag & FLAG_EVENT) != 0) {
//                res.setEvent(Response.HEARTBEAT_EVENT);
//            }
//            byte status = header[3];
//            res.setStatus(status);
//            if (status == Response.OK) {
//                try {
//                    Object data;
//                    if (res.isHeartbeat()) {
//                        data = s.deserialize(is).readObject();
//                    } else if (res.isEvent()) {
//                        data = s.deserialize(is).readObject();
//                    } else {
//                        DecodeableRpcResult result = new DecodeableRpcResult(res, is, (Invocation)getRequestData(id), proto);
//                        result.decode();
//                        data = result;
//                    }
//                    res.setResult(data);
//                } catch (Throwable t) {
//                    res.setStatus(Response.CLIENT_ERROR);
//                    res.setErrorMessage(t.getMessage());
//                }
//            } else {
//                res.setErrorMessage(s.deserialize(is).readUTF());
//            }
//            return res;
//        } else {
//            Request req = new Request(id);
//            req.setVersion("2.0.0");
//            req.setTwoWay((flag & FLAG_TWOWAY) != 0);
//            if ((flag & FLAG_EVENT) != 0) {
//                req.setEvent(Request.HEARTBEAT_EVENT);
//            }
//            try {
//                Object data;
//                if (req.isHeartbeat()) {
//                    data = s.deserialize(is).readObject();
//                } else if (req.isEvent()) {
//                    data = s.deserialize(is).readObject();
//                } else {
//                    DecodeableRpcInvocation inv = new DecodeableRpcInvocation(req, is, proto);
//                    inv.decode();
//                    data = inv;
//                }
//                req.setData(data);
//            } catch (Throwable t) {
//                req.setBroken(true);
//                req.setData(t);
//            }
//            return req;
//        }
//    }
//}
