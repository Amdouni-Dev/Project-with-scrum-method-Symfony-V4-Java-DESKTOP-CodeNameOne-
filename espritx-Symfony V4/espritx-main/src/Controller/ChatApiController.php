<?php

namespace App\Controller;

use App\Entity\Channel;
use App\Entity\Message;
use App\Entity\User;
use Doctrine\ORM\EntityManager;
use Doctrine\ORM\EntityManagerInterface;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\ParamConverter;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Serializer\Normalizer\AbstractNormalizer;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\Serializer\Serializer;

class ChatApiController extends AbstractApiController
{
    /**
     * @Route("/api/showChannel", name="Api-show-channel", methods={"GET"})
     */
    public function ChannelsApi(EntityManagerInterface $entityManager)
    {
        $channels = $entityManager->getRepository(Channel::class)->findAll();
        return $this->json($channels);
    }

    /**
     * @Route("/api/addChannelapi", name="addChannelapi", methods={"GET","POST"})
     * @return Response
     */
    public function addChannelapi(Request $request,EntityManagerInterface $entityManager): Response
    {
        $data = $request->request->get("participants");
        $channel = new Channel();
        $user1=$this->getDoctrine()->getManager()->getRepository(User::class)->find($data[0]["id"]);
        $user2=$this->getDoctrine()->getManager()->getRepository(User::class)->find($data[1]["id"]);
      $channel->addParticipant($user1);
        $channel->addParticipant($user2);
            $entityManager->persist($channel);
            $entityManager->flush();
return $this->json($channel);
    }
    /**
     * @Route("/api/DeleteChannelApi", name="DeleteChannelApi", methods={"GET","POST"})
     * @return Response
     */
    public function DeleteChannelApi(Request $request,EntityManagerInterface $entityManager): Response
    {
        $conversationid = $request->request->get("id");
        $entityManager->remove($this->getDoctrine()->getManager()->getRepository(Channel::class)->find($conversationid));
        $entityManager->flush();

        return $this->json($this->getDoctrine()->getManager()->getRepository(Channel::class)->find(25));
    }
    /**
     * @Route("/api/UpdateChannelApi", name="UpdateChannelApi", methods={"GET","POST"})
     * @return Response
     */
    public function UpdateChannelApi(Request $request,EntityManagerInterface $entityManager): Response
    {
        $conversationid = $request->request->get("id");
        $participants = $request->request->get("participants");
        /*Static Until configured*/
        $convo=$this->getDoctrine()->getManager()->getRepository(Channel::class)->find(25);
      $user1totest=$convo->getParticipants()->get(0);
        $user2totest=$convo->getParticipants()->get(1);
        $convo->removeParticipant($user1totest);
        $convo->removeParticipant($user2totest);
        $convo->addParticipant($this->getDoctrine()->getManager()->getRepository(User::class)->find($participants[0]["id"]));
        $convo->addParticipant($this->getDoctrine()->getManager()->getRepository(User::class)->find($participants[1]["id"]));
$entityManager->persist($convo);
        $entityManager->flush();

        return $this->json($convo);
    }
    /**
     * @Route("/api/showMessages", name="Api-show-messages", methods={"GET"})
     */
    public function Messagesapi(EntityManagerInterface $entityManager)
    {
        $messages = $entityManager->getRepository(Message::class)->findAll();
        return $this->json($messages);
    }
    /**
     * @Route("/api/AddMessage", name="api-AddMessage", methods={"GET","POST"})
     * @return Response
     */
    public function AddMessage(Request $request,EntityManagerInterface $entityManager): Response
    {
        $author = $request->request->get("author");
        $content = $request->request->get("content");
        $idchannel = $request->request->get("Conversationid");
        $message = new Message();
        $message->setAuthor($this->getDoctrine()->getManager()->getRepository(User::class)->find($author["id"]));
        $message->setChannel($this->getDoctrine()->getManager()->getRepository(Channel::class)->find($idchannel));
        $message->setContent($content);
        $entityManager->persist($message);
        $entityManager->flush();
        return $this->json($message);
    }
    /**
     * @Route("/api/DeleteMessage", name="api-DeleteMessage", methods={"GET","POST"})
     * @return Response
     */
    public function DeleteMessage(Request $request,EntityManagerInterface $entityManager): Response
    {
        $id = $request->request->get("id");
        $message=$this->getDoctrine()->getManager()->getRepository(Message::class)->find($id);
        $entityManager->remove($message);
        $entityManager->flush();
        return $this->json($this->getDoctrine()->getManager()->getRepository(Channel::class)->findAll());

    }
    /**
     * @Route("/api/EditMessage", name="api-EditMessage", methods={"GET","POST"})
     * @return Response
     */
    public function EditMessage(Request $request,EntityManagerInterface $entityManager): Response
    {
        $id = $request->request->get("id");
        $author = $request->request->get("author");
        $content = $request->request->get("content");
        $idchannel = $request->request->get("Conversationid");
        $message = $this->getDoctrine()->getManager()->getRepository(Message::class)->find(100);
        $message->setAuthor($this->getDoctrine()->getManager()->getRepository(User::class)->find($author["id"]));
        $message->setChannel($this->getDoctrine()->getManager()->getRepository(Channel::class)->find($idchannel));
        $message->setContent($content);
        $entityManager->persist($message);
        $entityManager->flush();
        return $this->json($this->getDoctrine()->getManager()->getRepository(Channel::class)->findAll());
    }

}
